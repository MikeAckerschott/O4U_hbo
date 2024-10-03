package nl.lpp.uncenshow.dto.user

class UserSignupDTO(
    var username: String?,
    private var password: String?,
    var image: String?
) {

    constructor() : this(null, null, null)

    fun hashPassword(): String {
        return password!!.hashCode().toString()
    }

    fun isValidPassword(): Boolean {
        val regexString = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}\$"
        val regex = Regex(regexString)

        return regex.matches(this.password!!)
    }

    fun isValidUsername(): Boolean {
        return this.username!!.length >= 4 && this.username!!.length <= 50
    }
}
