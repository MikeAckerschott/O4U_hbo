package nl.han.devops.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nl.han.devops.service.AuthenticationService;
import org.springframework.web.servlet.HandlerInterceptor;

public class TokenInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    public TokenInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith("/login") || requestURI.endsWith("/signup") || requestURI.endsWith("/image")) {
            return true;
        }

        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token missing");
            return false;
        }

        if (!authenticationService.tokenExists(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }

        return true;
    }
}

