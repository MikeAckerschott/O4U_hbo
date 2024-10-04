package nl.han.devops.service;

import nl.han.devops.dto.ProjectDTO;
import nl.han.devops.dto.StudentDTO;
import nl.han.devops.dto.UserDTO;
import nl.han.devops.repositories.ProjectRepository;
import nl.han.devops.repositories.StudentRepository;
import nl.han.devops.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(UserRepository userRepository, ProjectRepository projectRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.studentRepository = studentRepository;
    }

    public ResponseEntity<String> currentProject(String token) {
        return null;
    }
}
