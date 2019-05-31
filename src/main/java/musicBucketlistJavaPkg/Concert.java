package musicBucketlistJavaPkg;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "concert")
//@JsonTypeName("concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}) //CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST})
    @JoinTable(name = "concert_has_user",
            joinColumns = @JoinColumn(name = "concert_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    @Column (name = "setlistId", unique = true, length = 20, nullable = false)
    private String setlistId;

    @Column (name = "artistName")
    private String artistName;

    @Column (name = "venue")
    private String venue;

    @Column (name = "city")
    private String city;

    @Column (name = "state")
    private String state;

    @Column (name = "date")
    private String date;

    @Column (name = "strDate")
    private String strDate;

    @Column (name = "setlist", length = 2000)
    private String setlist;
    //private Set<String> set = new HashSet<>();

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

//    public String getStrDate() {
//        return strDate;
//    }

    public void setDate(String date) {
        this.date = date;
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("MTN"));
//        this.strDate = dateFormat.format(date);
    }

    public String getSetlist() {
        return setlist;
    }

    public void setSetlist(String setlist) {
        this.setlist = setlist;
    }

    public String getSetlistId() {
        return setlistId;
    }

    public void setSetlistId(String setlistId) {
        this.setlistId = setlistId;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Long getId() {
        return id;
    }
}