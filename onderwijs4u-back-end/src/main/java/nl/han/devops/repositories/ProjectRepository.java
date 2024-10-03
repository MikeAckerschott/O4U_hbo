package nl.han.devops.repositories;

import nl.han.devops.dto.ProjectDTO;
import nl.han.devops.dto.UserDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectDTO, Integer> {
    Optional<ProjectDTO> findByProject_id(String project_id);
    Optional<ProjectDTO> findByStudent_id(String student_id);
    Optional<ProjectDTO> findByName(String name);
}