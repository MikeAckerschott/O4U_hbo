package nl.lpp.uncenshow.dto.user

import java.security.SecureRandom
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("user")
class UserDTO(
        @Id var id: String?,
        private var token: String?,
        var username: String?,
        private var password: String?,
        var image: String?
) {

    constructor() : this(null, null, null, null, null)

    fun hashPassword() {
        password = password!!.hashCode().toString()
    }

    fun getToken(): String? {
        return token
    }
    fun setToken(token: String) {
        this.token = token
    }

    fun generateRandomSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }
}
