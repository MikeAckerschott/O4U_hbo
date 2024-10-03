package nl.lpp.uncenshow.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import nl.lpp.uncenshow.service.authentication.AuthenticationService
import org.springframework.web.servlet.HandlerInterceptor

class TokenInterceptor (private val authenticationService: AuthenticationService) : HandlerInterceptor {
    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        val requestURI = request.requestURI
        if (requestURI.endsWith("/login") || requestURI.endsWith("/signup") || requestURI.endsWith("/image")) {
            return true
        }

        val token = request.getParameter("token")
        if (token == null || token.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token missing")
            return false
        }

        if (!authenticationService.tokenExists(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token")
            return false
        }

        return true
    }
}