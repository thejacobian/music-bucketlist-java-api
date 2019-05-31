package musicBucketlistJavaPkg;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

@RestController
//@JsonTypeName("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private UserService userService;

    //at the top of the class definition near userRepository and userService:
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // INDEX Route
    @GetMapping("/user")
    public Iterable<User> getUsers(){
        System.out.println("-----------------------------------------------");
        System.out.println("***GET from /user Index Route is activated!***");
        System.out.println("-----------------------------------------------");
        return userRepository.findAll();
    }

    // SHOW Route
    @GetMapping("/user/{id}")
    public User showUser(@PathVariable("id") Long id, HttpSession session) throws Exception {
        System.out.println("-----------------------------------------------");
        System.out.println("***GET from /user/id Show Route is activated!***");
        System.out.println("-----------------------------------------------");
        try {
            User userSession = userRepository.findByUsername(session.getAttribute("username").toString());
            if (userSession == null) {
                throw new Exception("You must be logged in to view users");
            }
            Optional<User> response = userRepository.findById(id);
            if (response.isPresent()) {
                return response.get();
            }
            throw new Exception("No such user");
            //return "showUser: Jake";
        } catch (Exception e) {
            System.out.println("Error when validating session user access to user data, please login again, error: " + e);
            return null;
        }
    }

//    // SHOW Route
//    @GetMapping("/user/{id}")
//    public User showUser(@PathVariable("id") Long id, HttpSession session) throws Exception {
//        System.out.println("-----------------------------------------------");
//        System.out.println("***GET from /user/id Show Route is activated!***");
//        System.out.println("-----------------------------------------------");
//        try {
//            User userSession = userRepository.findByUsername(session.getAttribute("username").toString());
//            if (userSession == null) {
//                throw new Exception("You must be logged in to view users");
//            }
//            Optional<User> response = userRepository.findById(id);
//            if (response.isPresent()) {
//                User userPath = response.get();
//                if (id == userSession.getId()) {
//                    return response.get();
//                } else {
//                    System.out.println("Unable to show user details for this user");
//                    return null;
//                }
//            }
//            throw new Exception("No such user");
//            //return "showUser: Jake";
//        } catch (Exception e) {
//            System.out.println("Error when validating session user access to user data, please login again, error: " + e);
//            return null;
//        }
//    }

    // SHOW CONCERTS Route
    @GetMapping("/user/profile/concerts")
//    @JsonTypeInfo(include=JsonTypeInfo.As.WRAPPER_OBJECT, use=JsonTypeInfo.Id.NAME)
    public Iterable<Concert> showConcerts(HttpSession session) throws Exception {
        //public String showUser() {
        System.out.println("----------------------------------------------------------");
        System.out.println("***GET from /user/id/concerts Index Route is activated!***");
        System.out.println("----------------------------------------------------------");
        try {
            User userSession = userRepository.findByUsername(session.getAttribute("username").toString());
            if (userSession == null) {
                throw new Exception("You must be logged in to view user concerts");
            }
            Iterable<Concert> resConcerts = concertRepository.findConcertsByUsers(userSession);
            int concertSize = 0;
            if (resConcerts instanceof Collection) {
                concertSize = ((Collection<?>) resConcerts).size();
            }
            if (concertSize > 0) {
                return resConcerts;
            }
            throw new Exception("No concerts for this user");
        } catch (Exception e) {
            System.out.println("Error viewing the user's concerts: " + e);
            return null;
        }
    }

//    // SHOW CONCERTS Route
//    @GetMapping("/user/{id}/concerts")
//    public Iterable<Concert> showConcerts(@PathVariable("id") Long id, HttpSession session) throws Exception {
//        //public String showUser() {
//        System.out.println("----------------------------------------------------------");
//        System.out.println("***GET from /user/id/concerts Index Route is activated!***");
//        System.out.println("----------------------------------------------------------");
//        try {
//            User userSession = userRepository.findByUsername(session.getAttribute("username").toString());
//            if (userSession == null) {
//                throw new Exception("You must be logged in to view user concerts");
//            }
//            Optional<User> resUser = userRepository.findById(id);
//            if (resUser.isPresent() && id == userSession.getId()) {
//                User userPath = resUser.get();
//                Iterable<Concert> resConcerts = concertRepository.findConcertsByUsers(userPath);
//                int concertSize = 0;
//                if (resConcerts instanceof Collection) {
//                    concertSize = ((Collection<?>) resConcerts).size();
//                }
//                if (concertSize > 0) {
//                    return resConcerts;
//                }
//                throw new Exception("No concerts for this user");
//            }
//            throw new Exception("No such user or unable to show concerts for this user");
//        } catch (Exception e) {
//            System.out.println("Error viewing the user's concerts: " + e);
//            return null;
//        }
//    }

    // SHOW WISHES Route
    @GetMapping("/user/profile/wishes")
    public Iterable<Wish> showWishes(HttpSession session) throws Exception {
        //public String showUser() {
        System.out.println("--------------------------------------------------------");
        System.out.println("***GET from /user/id/wishes Index Route is activated!***");
        System.out.println("--------------------------------------------------------");
        try {
            User userSession = userRepository.findByUsername(session.getAttribute("username").toString());
            if (userSession == null) {
                throw new Exception("You must be logged in to view user wishes");
            }
            Iterable<Wish> resWishes = wishRepository.findWishesByUsers(userSession);
            int wishSize = 0;
            if (resWishes instanceof Collection) {
                wishSize = ((Collection<?>) resWishes).size();
            }
            if (wishSize > 0) {
                return resWishes;
            }
            throw new Exception("No wishes for this user");
        } catch (Exception e) {
            System.out.println("Error viewing the user's wishes: " + e);
            return null;
        }
    }

//    // SHOW WISHES Route
//    @GetMapping("/user/{id}/wishes")
//    public Iterable<Wish> showWishes(@PathVariable("id") Long id, HttpSession session) throws Exception {
//        //public String showUser() {
//        System.out.println("--------------------------------------------------------");
//        System.out.println("***GET from /user/id/wishes Index Route is activated!***");
//        System.out.println("--------------------------------------------------------");
//        try {
//            User userSession = userRepository.findByUsername(session.getAttribute("username").toString());
//            if (userSession == null) {
//                throw new Exception("You must be logged in to view user wishes");
//            }
//            Optional<User> resUser = userRepository.findById(id);
//            if (resUser.isPresent() && id == userSession.getId()) {
//                User userPath = resUser.get();
//                Iterable<Wish> resWishes = wishRepository.findWishesByUsers(userPath);
//                int wishSize = 0;
//                if (resWishes instanceof Collection) {
//                    wishSize = ((Collection<?>) resWishes).size();
//                }
//                if (wishSize > 0) {
//                    return resWishes;
//                }
//                throw new Exception("No wishes for this user");
//            }
//            throw new Exception("No such user or unable to show wishes for this user");
//        } catch (Exception e) {
//            System.out.println("Error viewing the user's wishes: " + e);
//            return null;
//        }
//    }

    // UPDATE Route
    @PutMapping("/user/{id}")
    public User updateUser(@RequestBody User formData, @PathVariable("id") long id, HttpSession session) throws Exception {
        System.out.println("--------------------------------------------------");
        System.out.println("***PUT from /user/id Update Route is activated!***");
        System.out.println("--------------------------------------------------");
        try {
            User userSession = userRepository.findByUsername(session.getAttribute("username").toString());
            if (userSession == null) {
                throw new Exception("You must be logged in to update user data");
            }
            Optional<User> resUser = userRepository.findById(id);
            if (resUser.isPresent() && id == userSession.getId()) {
                User userPath = resUser.get();
                userPath.setUsername(formData.getUsername());
                userPath.setPassword(formData.getPassword());
                userPath.setLocation(formData.getLocation());
                return userService.saveUser(userPath);
            }
            throw new Exception("No such user or unable to update data for this user");
            //return "updateUser: updatedJake";
        } catch (Exception e) {
            System.out.println("Error when updating the user's data: " + e);
            return null;
        }
    }

    // DELETE Route
    @DeleteMapping("/user/{id}")
    public String delete(@PathVariable("id") Long id, HttpSession session){
        System.out.println("-----------------------------------------------------");
        System.out.println("***DELETE from /user/id Delete Route is activated!***");
        System.out.println("-----------------------------------------------------");
        try {
            User userSession = userRepository.findByUsername(session.getAttribute("username").toString());
            if (userSession == null) {
                throw new Exception("You must be logged in to delete your account");
            }
            Optional<User> resUser = userRepository.findById(id);
            if (resUser.isPresent() && id == userSession.getId()) {
                User userPath = resUser.get();

                // remove the user from the concerts attended that they may have had
                Iterable<Concert> resConcerts = concertRepository.findConcertsByUsers(userPath);
                for (Concert resConcert : resConcerts) {
                    if (resConcert.getUsers().size() == 1) {
                        concertRepository.delete(resConcert);
                        System.out.println("Deleted a concert that had id: " + id);
                    } else {
                        resConcert.getUsers().remove(userPath);
                        concertRepository.save(resConcert);
                        System.out.println("Deleted a user from a concert that had user id: " + id);
                    }
                }

                // remove the user from the wished for Artists that they may have had
                Iterable<Wish> resWishes = wishRepository.findWishesByUsers(userPath);
                for (Wish resWish : resWishes) {
                    if (resWish.getUsers().size() == 1) {
                        wishRepository.delete(resWish);
                        System.out.println("Deleted a wish that had id: " + id);
                    } else {
                        resWish.getUsers().remove(userPath);
                        wishRepository.save(resWish);
                        System.out.println("Deleted a user from a wish that had user id: " + id);
                    }
                }

                // delete the user
                userRepository.deleteById(id);
                return "Deleted a user and all associated data that had id: " + id;
            }
            throw new Exception("No such user or unable to update data for this user");
            //return "deletedUser: deletedJake";
        } catch (Exception e) {
            System.out.println("Error when deleting the user: " + e);
            return null;
        }
    }

    // CREATE/REGISTER Route
    @PostMapping("/auth/register")
    public User createUser(@RequestBody User user, HttpSession session){
        System.out.println("---------------------------------------------------------");
        System.out.println("***POST from /user Create/Register Route is activated!***");
        System.out.println("---------------------------------------------------------");
        try {
            User newUser = userService.saveUser(user);
            if (newUser != null) {
                session.setAttribute("username", newUser.getUsername());
            }
            return newUser;
        } catch (Exception e) {
            System.out.println("Unable to create new user with error: " + e);
            return  null;
        }
    }

    // LOGIN route
    @PostMapping("/auth/login")
    public User loginUser(@RequestBody User login, HttpSession session) throws IOException {
        System.out.println("-----------------------------------------------");
        System.out.println("***POST from /user Login Route is activated!***");
        System.out.println("-----------------------------------------------");
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        try {
            User user = userRepository.findByUsername(login.getUsername());
            if (user == null) {
                throw new IOException("Invalid Credentials");
            }
            boolean valid = bCryptPasswordEncoder.matches(login.getPassword(), user.getPassword());
            if (valid) {
                session.setAttribute("username", user.getUsername());
                return user;
            } else {
                throw new IOException("Invalid Credentials");
            }
        } catch (Exception e) {
            System.out.println("Unable to login with error: " + e);
            return null;
        }
    }

    // LOGOUT route
    @PostMapping("/auth/logout")
    public boolean logout(HttpSession session) {
        System.out.println("------------------------------------------------");
        System.out.println("***POST from /user Logout Route is activated!***");
        System.out.println("------------------------------------------------");
        if(session !=null) {
            try {
                session.removeAttribute("username");
                session.invalidate();
                return true;
            } catch (Exception e) {
                System.out.println("Unable to invalidate session with Error: " + e);
                return false;
            }
        } else {
            // session already null/expired/invalidated so return true
            return true;
        }
    }
}
