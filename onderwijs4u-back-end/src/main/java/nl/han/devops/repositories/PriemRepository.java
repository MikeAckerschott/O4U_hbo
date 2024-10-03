package nl.han.devops.repositories;

import nl.han.devops.dto.Priem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriemRepository extends CrudRepository<Priem, Integer> {

}
