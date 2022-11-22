import java.time.Instant

class JWTInvalidPartCountException(message: String) : Exception(message)

/**
*  Protocol that defines what a decoded JWT token should be.
*/
interface JWT {
    /// token header part contents
    var header: Map<String, Any>
    /// token body part values or token claims
    var body: Map<String, Any>
    /// token signature part
    var signature: String?
    /// jwt string value
    var string: String

    /// value of `exp` claim if available
    var expiresAt: Instant?
    /// value of `iss` claim if available
    var issuer: String?
    /// value of `sub` claim if available
    var subject: String?
    /// value of `aud` claim if available
    var audience: Array<String>?
    /// value of `iat` claim if available
    var issuedAt: Instant?
    /// value of `nbf` claim if available
    var notBefore: Instant?
    /// value of `jti` claim if available
    var identifier: String?

    /// Checks if the token is currently expired using the `exp` claim. If there is no claim present it will deem the token not expired
    var expired: Boolean

    fun claim(name: String): Claim {
        val value = body[name]
        return Claim(value)
    }
}
