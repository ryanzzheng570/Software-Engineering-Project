package ShopifyProj;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Merchant {
    private int id;
    private List<Shop> shops;
    private String name;
    private String contactPhoneNumber;
    private String email;
    private int rating;

    private static final AtomicLong counter = new AtomicLong();

    public Merchant() {
    }

    public Merchant(String name, String contactPhoneNumber, String email) {
        this.name = name;
        this.contactPhoneNumber = contactPhoneNumber;
        this.email = email;
        //Begin with 0 rating
        rating = 0;
    }

    public Merchant(List<Shop> shops, String name, String contactPhoneNumber, String email) {
        this.id = Math.toIntExact(counter.incrementAndGet());

        this.shops = shops;
        this.name = name;
        this.contactPhoneNumber = contactPhoneNumber;
        this.email = email;
        //Begin with 0 rating
        rating = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Shop> getShops() {
        return shops;
    }

    public void setShops(List<Shop> shops) {
        this.shops = shops;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public static AtomicLong getCounter() {
        return counter;
    }
}
