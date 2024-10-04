package nl.han.devops.repositories;

import nl.han.devops.dto.StudentDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository<StudentDTO, Integer> {
    Optional<StudentDTO> findByUser_id(int userId);
}