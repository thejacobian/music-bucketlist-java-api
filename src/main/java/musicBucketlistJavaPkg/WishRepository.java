package musicBucketlistJavaPkg;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called postRepository
// CRUD refers Create, Read, Update, Delete
//@JsonTypeName("wishes")
public interface WishRepository extends CrudRepository<Wish, Long> {
    Iterable<Wish> findWishesByUsers(User user);
    Optional<Wish> findByArtistId(String artistId);
}
