package nl.han.devops.controller;

import nl.han.devops.dto.UserSignupDTO;
import nl.han.devops.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody UserSignupDTO user) {
        return userService.signUp(user);
    }

    @GetMapping("/login")
    public String login(@RequestBody UserSignupDTO user) {
        return userService.login(user);
    }

}
