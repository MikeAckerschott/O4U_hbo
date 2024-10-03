package nl.lpp.uncenshow.exceptionhandling.exceptions

import nl.lpp.uncenshow.exceptionhandling.BaseException
import org.springframework.http.HttpStatus

class UsernameAlreadyExistsException : BaseException("Username already exists.") {
    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.CONFLICT
    }
}