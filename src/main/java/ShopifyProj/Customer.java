package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Customer {

    private static final AtomicLong counter = new AtomicLong();
    private int id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private String note;
    private Set<Item> boughItems;
    private Set<Item> cart;

    @Autowired
    public Customer() {
    }

    public Customer(String name, String email, String address, String phoneNumber, String note,
                    Set<Item> boughItems, Set<Item> cart) {
        this.id = Math.toIntExact(counter.incrementAndGet());
        this.name = name;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.note = note;
        this.boughItems = boughItems;
        this.cart = cart;
    }

    public void appendNewBoughItem(Item item) {
        boughItems.add(item);
    }

    public void appendNewCartItem(Item item) {
        cart.add(item);
    }

    public Item getBoughItemById(int itemId) {
        for (Iterator<Item> it = boughItems.iterator(); it.hasNext(); ) {
            Item item = it.next();
            if (item.getId() == itemId) {
                return item;
            }
        }

        //Return an empty for now, decide what to return if the bough item is not found later
        return new Item();
    }

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public Set<Item> getBoughItems() {
        return boughItems;
    }

    public void setBoughItems(Set<Item> boughItems) {
        this.boughItems = boughItems;
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public Set<Item> getCart() {
        return cart;
    }

    public void setCart(Set<Item> cart) {
        this.cart = cart;
    }
}
