package ShopifyProj.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Customer extends User{

    private static final AtomicLong counter = new AtomicLong();
    private String id;
    private String email;
    private String address;
    private String phoneNumber;
    private String note;
    private Set<Item> boughtItems;

    @Autowired
    public Customer() {
        super();
    }

    public Customer(String id) {
        this("", "", "", "", "", new HashSet<Item>(), "");
        this.id = id;
    }

    public Customer(String userName, String password) {
        super(userName, password);
    }

    public Customer(String name, String email, String address, String phoneNumber, String note,
                    Set<Item> boughtItems, String password) {
        super(name, password);
        this.id = Integer.toString(Math.toIntExact(counter.incrementAndGet()));
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.note = note;
        this.boughtItems = boughtItems;
    }
    /*
        Append new item to the existing set
     */
    public void appendNewBoughtItem(Item item) {
        boughtItems.add(item);
    }



    /*
        Get bough item using Id
     */
    public Item getBoughtItemById(String itemId) {
        return itemSetIterator(boughtItems, itemId);
    }


    private Item itemSetIterator(Set<Item> set, String itemId) {
        for (Item item : set) {
            if (item.getId().equals(itemId)) {
                return (item);
            }
        }
        return (null);
    }

    public void removeBoughtItemById(String itemId) {
        Item toRemove = getBoughtItemById(itemId);
        if(toRemove != null) {
            this.boughtItems.remove(toRemove);
        }
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return super.getUserName();
    }

    public void setName(String name) {
        super.setUserName(name);
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
    public Set<Item> getBoughtItems() {
        return boughtItems;
    }

    public void setBoughtItems(Set<Item> boughtItems) {
        this.boughtItems = boughtItems;
    }
}
