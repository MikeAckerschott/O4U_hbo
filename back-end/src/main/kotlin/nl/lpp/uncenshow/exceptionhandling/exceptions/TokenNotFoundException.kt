package nl.lpp.uncenshow.exceptionhandling.exceptions

import nl.lpp.uncenshow.exceptionhandling.BaseException
import org.springframework.http.HttpStatus

class TokenNotFoundException : BaseException("Token not found.") {
    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.NOT_FOUND
    }
}