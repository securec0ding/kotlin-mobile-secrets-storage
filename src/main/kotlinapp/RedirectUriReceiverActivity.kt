package kotlinapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*

class RedirectUriReceiverActivity : AppCompatActivity() {

    suspend fun makeTokenRequest(code: String) {
        val client = HttpClient() {
            install(JsonFeature)
        }
        val platformDomain: String = System.getenv("PLATFORM_DOMAIN")
        val origin = "https://" + platformDomain
        val response: TokenResponse = client.submitForm(
            url = OAuthHelper.tokenEndpoint,
            formParameters = Parameters.build {
                append("client_id", OAuthHelper.clientId)
                append("grant_type", "authorization_code")
                append("redirect_uri", OAuthHelper.redirectUri)
                append("code", code)
                append("client_secret", OAuthHelper.clientSecret)
            }
        ) {
            header(HttpHeaders.Origin, origin)
        } 

        val token = response.access_token
        toast("Token $token")
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = getIntent().getData()

        var code = ""
        response?.getQueryParameter("code")?.let {
            code = it
        }

        GlobalScope.launch {
            makeTokenRequest(code)
        }

    }

}