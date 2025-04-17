package nl.han.devops.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import nl.han.devops.dto.CrebosDTO;

import java.util.Optional;

@Repository
public interface CrebosRepository extends CrudRepository<CrebosDTO, String> {
    Optional<CrebosDTO> findByCrebonumber(String crebonumber);
}

