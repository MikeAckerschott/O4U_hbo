package nl.lpp.uncenshow.service.authentication

import nl.lpp.uncenshow.datasource.authentication.AuthenticationDAO
import org.springframework.stereotype.Service

@Service
class AuthenticationService (private val authenticationDAO: AuthenticationDAO) {

    fun tokenExists(token: String): Boolean {
        return authenticationDAO.tokenExists(token)
    }
}