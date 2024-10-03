package nl.lpp.uncenshow.datasource.authentication

import nl.lpp.uncenshow.dto.user.UserRepository
import org.springframework.stereotype.Component

@Component
class AuthenticationDAO (private val userRepository: UserRepository){

    fun tokenExists(token: String): Boolean {
        return userRepository.findItemByToken(token) != null
    }
}