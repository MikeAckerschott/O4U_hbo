package nl.lpp.uncenshow.exceptionhandling

import org.springframework.http.HttpStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class BaseException(message: String) : RuntimeException(message) {

    open fun handleException(): ErrorResponse {
        return ErrorResponse(
            status = getHttpStatus().value(),
            message = message ?: getDefaultMessage(),
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }

    open fun getHttpStatus(): HttpStatus {
        return HttpStatus.INTERNAL_SERVER_ERROR
    }

    open fun getDefaultMessage(): String {
        return "Unknown error"
    }
}
