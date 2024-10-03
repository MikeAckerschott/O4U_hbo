package nl.han.devops.dto;

public class UserSignupDTO {

    private String username;

    private String password;

    UserSignupDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
