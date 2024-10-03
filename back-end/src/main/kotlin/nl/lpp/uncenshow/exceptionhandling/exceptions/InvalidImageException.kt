package nl.lpp.uncenshow.exceptionhandling.exceptions

import nl.lpp.uncenshow.exceptionhandling.BaseException
import org.springframework.http.HttpStatus

class InvalidImageException(s: String) : BaseException(s) {

    constructor() : this("Invalid image.")

    override fun getHttpStatus(): HttpStatus {
        return HttpStatus.NOT_FOUND
    }
}