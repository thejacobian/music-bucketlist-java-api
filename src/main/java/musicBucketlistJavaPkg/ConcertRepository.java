package musicBucketlistJavaPkg;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called postRepository
// CRUD refers Create, Read, Update, Delete
//@JsonTypeName("concerts")
public interface ConcertRepository extends CrudRepository<Concert, Long> {
    Iterable<Concert> findConcertsByUsers(User user);
    Optional<Concert> findBySetlistId(String setlistId);
}
