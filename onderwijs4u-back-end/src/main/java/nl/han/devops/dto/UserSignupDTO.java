package nl.han.devops.dto;

public class UserSignupDTO {

    private String email;

    private String password;

    UserSignupDTO(String username, String password) {
        this.email = username;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
