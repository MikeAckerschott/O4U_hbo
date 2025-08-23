package nl.han.devops.service;

public class PasswordEncoder {
    public String encode(String password) {
        return password; //TODO: Implement password hashing
    }

    public boolean matches(String password, String password1) {
        return encode(password).equals(password1);
    }
}
