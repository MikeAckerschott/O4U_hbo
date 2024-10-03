package nl.han.devops.service;

import nl.han.devops.data.AuthenticationDAO;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationDAO authenticationDAO;

    public AuthenticationService(AuthenticationDAO authenticationDAO) {
        this.authenticationDAO = authenticationDAO;
    }

    public boolean tokenExists(String token) {
        return authenticationDAO.tokenExists(token);
    }
}

