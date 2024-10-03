package nl.han.devops.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationPropertiesChecker {

    private final Environment environment;

    public ApplicationPropertiesChecker(Environment environment) {
        this.environment = environment;
        System.out.println("Active Profiles: " + String.join(",", environment.getActiveProfiles()));
    }

    @PostConstruct
    public void init() {
        System.out.println("Server port: " + environment.getProperty("server.port"));
    }
}
