import java.time.Instant
import java.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 *  JWT Claim
 */
data class Claim(val value: Any?) {

    /// original claim value
    public val rawValue: Any?
        get() = this.value

    /// value of the claim as `String`
    public val string: String?
        get() = this.value as? String

     /// value of the claim as `Bool`
    public val boolean: Boolean?
        get() = this.value as? Boolean

    /// value of the claim as `Double`
    public val double: Double?
        get() {
            var double: Double? = null
            if (this.string != null) {
                double = this.string?.toDouble()
            } else if (this.boolean == null) {
                double = this.value as? Double
            }
            return double
        }

    /// value of the claim as `Int`
    public val integer: Int?
        get() {
            var integer: Int? = null
            if (this.string != null) {
                integer = this.string?.toInt()
            } else if (this.boolean == null) {
                integer = this.value as? Int
            }
            return integer
        }

    /// value of the claim as `Long`
    public val long: Long?
        get() {
            var long: Long? = null
            if (this.string != null) {
                long = this.string?.toLong()
            } else if (this.boolean == null) {
                long = this.value as? Long
            }
            return long
        }

    /// value of the claim as `Instant`
    public val date: Instant?
        get() {
            val l = this.long
            if (l != null) {
                return Instant.ofEpochMilli(l)
            } else {
                return null
            }
        }

    /// value of the claim as `[String]`
    public val array: Array<String>?
        get() {
            var array: Array<String>?
            @Suppress("UNCHECKED_CAST")
            array = this.value as? Array<String>?
            if (array != null) return array

            var value: String?
            value = this.value as? String
            if (value != null) return arrayOf(value)

            return null
        }
}

object JWTFactory {
    public fun decodeJWT(jwt: String): JWT {
        return DecodedJWT(jwt)
    }

}

class DecodedJWT : JWT {
    override var header: Map<String, Any> = HashMap<String, Any>()
    override var body: Map<String, Any> = HashMap<String, Any>()
    override var signature: String? = null
    override var string: String = ""

    constructor(jwt: String) {
        val parts = jwt.split(".").toTypedArray()
        if (parts.size != 3) {
            throw JWTInvalidPartCountException(parts.size.toString())
        }
        this.header = decodeJWTPart(parts[0])
        this.body = decodeJWTPart(parts[1])
        this.signature = parts[2]
        this.string = jwt
    }

    override var expiresAt: Instant? = null
      get() = claim("exp").date

    override var issuer: String? = null
        get() = claim("iss").string

    override var subject: String? = null
        get() = claim("sub").string

    override var audience: Array<String>? = null
        get() = claim("aud").array

    override var issuedAt: Instant? = null
        get() = claim("iat").date

    override var notBefore: Instant? = null
        get() = claim("nbf").date

    override var identifier: String? = null
        get() = claim("jti").string

    override var expired: Boolean = false
        get() {
            val eat = this.expiresAt
            if (eat == null) return false
            return eat < Instant.now()
        }


    private fun base64UrlDecode(value: String): String {

        val decodedBytes = Base64.getDecoder().decode(value)
        val decodedString = String(decodedBytes)

        return decodedString
    }

    private fun decodeJWTPart(value: String): Map<String, Any> {
        val json = base64UrlDecode(value)

        val gson = Gson()

        var tutorialMap: Map<String, Any> = gson.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)

        return tutorialMap
    }
}

