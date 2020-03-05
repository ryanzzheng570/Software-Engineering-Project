package ShopifyProj;

import org.springframework.stereotype.Component;

import javax.persistence.*;
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

    /*
        Add new shop to existing shops
     */
    public void appendNewShops(Shop newShop) {
        shops.add(newShop);
    }

    /*
        Find the new shops using shop id
     */
    public Shop getShopById(int id) {
        for(int i=0; i<shops.size(); i++) {
            Shop shop = shops.get(i);
            if( shop.getId() == id){
                return shop;
            }
        }

        return null;
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
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
