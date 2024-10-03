package nl.lpp.uncenshow.controller

import nl.lpp.uncenshow.dto.user.UserDTO
import nl.lpp.uncenshow.dto.user.UserSignupDTO
import nl.lpp.uncenshow.service.authentication.AuthenticationService
import nl.lpp.uncenshow.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/user")
class UserController
@Autowired
constructor(
        private val userService: UserService,
        private val authenticationService: AuthenticationService
) {

    @RequestMapping()
    @GetMapping(produces = ["application/json"])
    fun getUser(@RequestParam token: String, @RequestParam id: String): UserDTO {
        return userService.getUser(token, id)
    }

    @RequestMapping("/signup")
    @PostMapping(produces = ["application/json"], consumes = ["application/json"])
    fun signUpUser(@RequestBody user: UserSignupDTO): UserDTO {
        return userService.signUpUser(user)
    }

    @RequestMapping("/login")
    @PostMapping(produces = ["application/json"], consumes = ["application/json"])
    fun loginUser(@RequestBody user: UserSignupDTO): UserDTO {
        return userService.loginUser(user)
    }

    @RequestMapping("/image")
    @PostMapping(produces = ["application/json"], consumes = ["multipart/form-data"])
    fun uploadImage(@RequestBody image: MultipartFile): String {
        return userService.uploadUserImage(image)
    }
}
