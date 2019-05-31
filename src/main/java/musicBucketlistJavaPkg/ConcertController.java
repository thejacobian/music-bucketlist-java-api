package musicBucketlistJavaPkg;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/concert")
//@JsonTypeName("concerts")
public class ConcertController {

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private UserRepository userRepository;

    // INDEX Route
    @GetMapping()
    public Iterable<Concert> getConcerts(){
        System.out.println("-----------------------------------------------");
        System.out.println("***GET from /concert Index Route is activated!***");
        System.out.println("-----------------------------------------------");
        //return "getConcerts: The Beatles, Red Rocks, 1964";
        return concertRepository.findAll();
    }

    // SHOW Route
    @GetMapping("/{id}")
    public Concert showConcert(@PathVariable("id") Long id) throws Exception {
        System.out.println("-----------------------------------------------");
        System.out.println("***GET from /concert/id Show Route is activated!***");
        System.out.println("-----------------------------------------------");
        //return "showConcert: The Beatles, Red Rocks, 1964";
        Optional<Concert> response = concertRepository.findById(id);
        if (response.isPresent()) {
            return response.get();
        }
        throw new Exception("No such concert");
    }

    // CREATE Route
    @PostMapping()
    public Concert createConcert(@RequestBody Concert request, HttpSession session) throws Exception {
        System.out.println("----------------------------------------------------");
        System.out.println("***POST from /concert createConcert Route is activated!***");
        System.out.println("----------------------------------------------------");
        try {
            User user = userRepository.findByUsername(session.getAttribute("username").toString());
            if (user == null) {
                throw new Exception("You must be logged in to add the concert");
            }
            Optional<Concert> response = concertRepository.findBySetlistId(request.getSetlistId());
            if(response.isPresent()) {
                Concert concert = response.get();
                if (concert.getUsers().add(user)) {
                    return concertRepository.save(concert);
                } else {
                    System.out.println("Unable to add the existing concert to the session user");
                    return null;
                }
            } else {
                Set<User> curConcertUsers = request.getUsers();
                if (curConcertUsers.add(user)) {
                    return concertRepository.save(request);
                } else {
                    System.out.println("Unable to add the new concert to the session user");
                    return null;
                }
            }
            // return "createConcert: The Beatles, Red Rocks, 1964";
        } catch (Exception e) {
            System.out.println("Error when validating session user access to create concert, please login again, error: " + e);
            return null;
        }
    }


    // UPDATE ROUTE
    @PutMapping("/{id}")
    public Concert updateConcert(@RequestBody Concert request, @PathVariable("id") Long id, HttpSession session) throws Exception {
        System.out.println("--------------------------------------------------");
        System.out.println("***PUT from /concert/id Route is activated!***");
        System.out.println("--------------------------------------------------");
        try {
            User user = userRepository.findByUsername(session.getAttribute("username").toString());
            if (user == null) {
                throw new Exception("You must be logged in to update the concert");
            }
            Optional<Concert> response = concertRepository.findById(id);
            if(response.isPresent()) {
                Concert concert = response.get();
                Set<User> curConcertUsers = concert.getUsers();
                if (curConcertUsers.contains(user)) {
                    concert.setArtistName(request.getArtistName());
                    concert.setVenue(request.getVenue());
                    concert.setCity(request.getCity());
                    concert.setState(request.getState());
                    concert.setDate(request.getDate());
                    concert.setSetlist(request.getSetlist());
                    return concertRepository.save(concert);
                } else {
                    System.out.println("The session user does not have access to this concert.");
                    return null;
                }
                // return "updateConcert: The Beatles, Red Rocks, 1964";
            }
            throw new Exception("No such concert");
        } catch (Exception e) {
            System.out.println("Error when validating session user access to concert, please login again, error: " + e);
            return null;
        }
    }

    // DELETE Route
    @DeleteMapping("/{id}")
    public Concert deleteConcert(@PathVariable("id") Long id, HttpSession session){
        System.out.println("--------------------------------------------------------");
        System.out.println("***DELETE from /concert/id Delete Route is activated!***");
        System.out.println("--------------------------------------------------------");
        try {
            User user = userRepository.findByUsername(session.getAttribute("username").toString());
            if (user == null) {
                throw new Exception("You must be logged in to delete the concert");
            }
            Optional<Concert> response = concertRepository.findById(id);
            if(response.isPresent()) {
                Concert concert = response.get();
                Set<User> curConcertUsers = concert.getUsers();
                if (curConcertUsers.contains(user) && curConcertUsers.size() == 1) {
                    concertRepository.deleteById(id);
                    System.out.println("Deleted a concert that had id: " + id);
                    return concert;
                } else if (curConcertUsers.size() > 1 && curConcertUsers.remove(user)) {
                    System.out.println("Deleted a concert that had id: " + id);
                    return concertRepository.save(concert);
                } else {
                    System.out.println("The session user does not have access to delete this concert.");
                    return null;
                }
                // return "deleteConcert: The Beatles, Red Rocks, 1964";
            }
            throw new Exception("No such concert");
        } catch (Exception e) {
            System.out.println("Error when validating session user access to concert, please login again, error: " + e);
            return null;
        }

    }
}
