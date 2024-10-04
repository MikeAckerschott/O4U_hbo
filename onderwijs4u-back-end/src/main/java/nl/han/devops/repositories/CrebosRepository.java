package nl.han.devops.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import nl.han.devops.dto.CrebosDTO;

import java.util.Optional;

@Repository
public interface CrebosRepository extends CrudRepository<CrebosDTO, String> {
    Optional<CrebosDTO> findByCrebo_number(String crebo_number);
    Optional<CrebosDTO> findByDescription(String description);
}

