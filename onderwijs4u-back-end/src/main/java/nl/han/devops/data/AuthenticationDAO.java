package nl.han.devops.data;

import nl.han.devops.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationDAO {
    private final UserRepository userRepository;

    public AuthenticationDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean tokenExists(String token) {
        return userRepository.findByToken(token).isPresent();
    }
}

