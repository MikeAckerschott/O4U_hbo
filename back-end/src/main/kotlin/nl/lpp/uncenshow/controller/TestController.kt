package nl.lpp.uncenshow.controller

import nl.lpp.uncenshow.service.authentication.AuthenticationService
import nl.lpp.uncenshow.service.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController
@Autowired
constructor(
    private val userService: UserService,
    private val authenticationService: AuthenticationService
) {

    @RequestMapping("/login")
    @GetMapping(produces = ["application/json"])
    fun test(): String {
        userService.test()
        return "test"
    }

}
