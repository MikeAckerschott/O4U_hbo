package nl.han.devops.service;

import nl.han.devops.dto.UserDTO;
import nl.han.devops.dto.UserSignupDTO;
import nl.han.devops.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new PasswordEncoder();
    }

    public String signUp(UserSignupDTO userSignupDTO) {
        String token = UUID.randomUUID().toString();

        UserDTO user = UserDTO.builder()
                .username(userSignupDTO.getUsername())
                .password(passwordEncoder.encode(userSignupDTO.getPassword()))
                .token(token)
                .build();

        userRepository.save(user);

        return token;
    }

    public String login(UserSignupDTO userSignupDTO) {
        UserDTO user = userRepository.findByUsername(userSignupDTO.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (passwordEncoder.matches(userSignupDTO.getPassword(), user.getPassword())) {
            user.setToken(UUID.randomUUID().toString());
            return user.getToken();
        } else {
            throw new IllegalArgumentException("Invalid password");
        }
    }
}
