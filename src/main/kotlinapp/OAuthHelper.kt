package kotlinapp

import android.net.Uri
import java.security.MessageDigest
import java.util.Base64
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token_type: String,
    val scope: String,
    val expires_in: Long,
    val ext_expires_in: Long,
    val access_token : String,
    val id_token: String)

object OAuthHelper {
    public val authEndpoint = "fillin"
    public val tokenEndpoint = "fillin"
    public val clientId = "fillin"
    public val clientSecret = "fillin"
    public val redirectUri = "veracoin://oauth"

    private val stateLength = 32
    private val codeVerifierLength = 128
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private var state = ""
    public var codeVerifier = ""
    private var codeChallenge = ""

    private fun randomString(len: Int) : String {
        val randomString = (1..len)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

        return randomString
    }

    fun generateState() : String {
        state = randomString(stateLength)
        return state
    }

    fun checkState(stateToCheck: String) : Boolean {
        println("Sent State: $state")
        return stateToCheck == state
    }

    fun createCodeChallenge(codeVerifier: String) : String {
        val digest = MessageDigest
            .getInstance("SHA-256")
            .digest(codeVerifier.toByteArray())

        var base64 = Base64.getUrlEncoder().encodeToString(digest)

        codeChallenge = base64.dropLast(1)

        return codeChallenge
    }

    fun generateCodeVerifier() : String {
        codeVerifier = randomString(codeVerifierLength)
        return codeVerifier
    }

    fun buildAuthUrl() : android.net.Uri? {
        val builtUri = Uri.parse(authEndpoint)
                .buildUpon()
                .appendQueryParameter("client_id", OAuthHelper.clientId)
                .appendQueryParameter("redirect_uri", OAuthHelper.redirectUri)
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("state", state)
                .appendQueryParameter("scope", "user.read openid profile email")
                .build()

        return builtUri
    }    

}