package nl.lpp.uncenshow.exceptionhandling

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: String
)