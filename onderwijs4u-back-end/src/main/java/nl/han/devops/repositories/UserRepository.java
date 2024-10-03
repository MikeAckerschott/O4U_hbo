package nl.han.devops.repositories;

import nl.han.devops.dto.UserDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserDTO, Integer> {
    Optional<UserDTO> findByToken(String token);
    Optional<UserDTO> findByUsername(String username);
}