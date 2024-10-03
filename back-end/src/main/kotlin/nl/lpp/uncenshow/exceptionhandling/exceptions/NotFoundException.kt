package nl.lpp.uncenshow.exceptionhandling.exceptions

import nl.lpp.uncenshow.exceptionhandling.BaseException
import org.springframework.http.HttpStatus

class NotFoundException : BaseException("Not found.") {
    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.NOT_FOUND
    }
}