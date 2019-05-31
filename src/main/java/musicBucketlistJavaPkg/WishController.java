package musicBucketlistJavaPkg;

import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/wish")
//@JsonTypeName("wishes")
public class WishController {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private UserRepository userRepository;

    // INDEX Route
    @GetMapping()
    public Iterable<Wish> getWishes(){
        System.out.println("-----------------------------------------------");
        System.out.println("***GET from /wish Index Route is activated!***");
        System.out.println("-----------------------------------------------");
        // return "getWishes: The Rolling Stones";
        return wishRepository.findAll();
    }

    // SHOW Route
    @GetMapping("/{id}")
    public Wish showWish(@PathVariable("id") Long id) throws Exception {
        System.out.println("-----------------------------------------------");
        System.out.println("***GET from /wish/id Show Route is activated!***");
        System.out.println("-----------------------------------------------");
        // return "showWish: The Rolling Stones";
        Optional<Wish> response = wishRepository.findById(id);
        if (response.isPresent()) {
            return response.get();
        }
        throw new Exception("No such wish");
    }

//    // SHOW Route
//    @GetMapping("/{id}")
//    public Wish showWish(@PathVariable("id") Long id, HttpSession session) throws Exception {
//        System.out.println("-----------------------------------------------");
//        System.out.println("***GET from /wish/id Show Route is activated!***");
//        System.out.println("-----------------------------------------------");
//        try {
//            User user = userRepository.findByUsername(session.getAttribute("username").toString());
//            if (user == null) {
//                throw new Exception("You must be logged in to show the wish");
//            }
//            Optional<Wish> response = wishRepository.findById(id);
//            if(response.isPresent()) {
//                Wish wish = response.get();
//                Set<User> curWishUsers = wish.getUsers();
//                if (curWishUsers.contains(user)) {
//                    return response.get();
//                } else {
//                    System.out.println("Unable to access the wish with the session user");
//                    return null;
//                }
//            }
//            throw new Exception("No such wish");
//            // return "showWish: The Rolling Stones";
//        } catch (Exception e) {
//            System.out.println("Error when validating session user access to create wish, please login again, error: " + e);
//            return null;
//        }
//    }

    // CREATE Route
    @PostMapping()
    public Wish createWish(@RequestBody Wish request, HttpSession session) throws Exception {
        System.out.println("----------------------------------------------------");
        System.out.println("***POST from /wish createWish Route is activated!***");
        System.out.println("----------------------------------------------------");
        try {
            User user = userRepository.findByUsername(session.getAttribute("username").toString());
            if (user == null) {
                throw new Exception("You must be logged in to add the wish");
            }
            Optional<Wish> response = wishRepository.findByArtistId(request.getArtistId());
            if(response.isPresent()) {
                Wish wish = response.get();
                if (wish.getUsers().add(user)) {
                    return wishRepository.save(wish);
                } else {
                    System.out.println("Unable to add the existing wish to the session user");
                    return null;
                }
            } else {
                Set<User> curWishUsers = request.getUsers();
                if (curWishUsers.add(user)) {
                    return wishRepository.save(request);
                } else {
                    System.out.println("Unable to add the new wish to the session user");
                    return null;
                }
            }
            // return "createWish: The Rolling Stones";
        } catch (Exception e) {
            System.out.println("Error when validating session user access to create wish, please login again, error: " + e);
            return null;
        }
    }


    // UPDATE ROUTE
    @PutMapping("/{id}")
    public Wish updateWish(@RequestBody Wish request, @PathVariable("id") Long id, HttpSession session) throws Exception {
        System.out.println("--------------------------------------------------");
        System.out.println("***PUT from /wish/id Route is activated!***");
        System.out.println("--------------------------------------------------");
        try {
            User user = userRepository.findByUsername(session.getAttribute("username").toString());
            if (user == null) {
                throw new Exception("You must be logged in to update the wish");
            }
            Optional<Wish> response = wishRepository.findById(id);
            if(response.isPresent()) {
                Wish wish = response.get();
                Set<User> curWishUsers = wish.getUsers();
                if (curWishUsers.contains(user)) {
                    wish.setArtistName(request.getArtistName());
                    wish.setArtistId(request.getArtistId());
                    return wishRepository.save(wish);
                } else {
                    System.out.println("The session user does not have access to this wish.");
                    return null;
                }
                // return "updateWish: The Rolling Stones";
            }
            throw new Exception("No such wish");
        } catch (Exception e) {
            System.out.println("Error when validating session user access to wish, please login again, error: " + e);
            return null;
        }
    }

    // DELETE Route
    @DeleteMapping("/{id}")
    public Wish deleteWish(@PathVariable("id") Long id, HttpSession session){
        System.out.println("-----------------------------------------------------");
        System.out.println("***DELETE from /wish/id Delete Route is activated!***");
        System.out.println("-----------------------------------------------------");
        try {
            User user = userRepository.findByUsername(session.getAttribute("username").toString());
            if (user == null) {
                throw new Exception("You must be logged in to delete the wish");
            }
            Optional<Wish> response = wishRepository.findById(id);
            if(response.isPresent()) {
                Wish wish = response.get();
                Set<User> curWishUsers = wish.getUsers();
                if (curWishUsers.contains(user) && curWishUsers.size() == 1) {
                    wishRepository.deleteById(id);
                    System.out.println("Deleted a wish that had id: " + id);
                    return wish;
                } else if (curWishUsers.size() > 1 && curWishUsers.remove(user)) {
                    return wishRepository.save(wish);
                } else {
                    System.out.println("The session user does not have access to delete this wish.");
                    return null;
                }
                // return "deletedWish: The Rolling Stones";
            }
            throw new Exception("No such wish");
        } catch (Exception e) {
            System.out.println("Error when validating session user access to wish, please login again, error: " + e);
            return null;
        }

    }
}
