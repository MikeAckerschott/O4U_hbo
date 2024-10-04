package nl.han.devops.controller;

import nl.han.devops.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/currentproject")
    @RequestMapping(produces = "application/json")
    public ResponseEntity<String> currentProject(@RequestParam String token) {
        return studentService.currentProject(token);
    }

}
