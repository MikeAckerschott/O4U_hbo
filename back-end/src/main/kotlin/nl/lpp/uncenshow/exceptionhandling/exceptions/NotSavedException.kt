package nl.lpp.uncenshow.exceptionhandling.exceptions

import nl.lpp.uncenshow.exceptionhandling.BaseException
import org.springframework.http.HttpStatus

class NotSavedException : BaseException("Not saved.") {
    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.INTERNAL_SERVER_ERROR
    }
}
