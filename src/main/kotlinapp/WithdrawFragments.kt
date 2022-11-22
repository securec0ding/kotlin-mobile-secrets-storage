//
//  WithdrawsFragment.kt
//  Veracoin
//

package kotlinapp

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.util.Log

import androidx.appcompat.app.Fragment

import firebase.FirebaseAnalytics

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders

import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class WithdrawResponse(
    val userid: String,
    val date: String,
    val amount: Int)

class WithdrawsFragment : BaseLoggingFragment() {

    companion object {
        private val TAG: String? = WithdrawsFragment::class.simpleName
    }

    override fun onCreateView(
            inflater: LayoutInflater, 
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_withdraws, container, false)
    }

    suspend fun makeApiRequest(userid: Int) {
         val client = HttpClient() {
            install(JsonFeature) {
                    serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                    })
            }
        }

        val platformDomain: String = System.getenv("PLATFORM_DOMAIN")
        val origin = "https://" + platformDomain
        val response: WithdrawResponse = client.get("https://FILLIN/api/withdraws/$userid") {
            headers {
                append(HttpHeaders.Origin, origin)
            }
        }

        val amt = response.amount
        toast("Amount $amt")
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userid = 0
        val currentUser = UserManager.currentUser
        if (currentUser != null) {
            userid = currentUser.id
        }        

        GlobalScope.launch {
            makeApiRequest(userid)
        }
       
    }

}