package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Item {
    private int id;

    private String itemName;

    private int inventory;

    private static final AtomicLong counter = new AtomicLong();

    @Autowired
    public Item() {
        this("");
    }

    public Item(String name) {
        this.id = Math.toIntExact(counter.incrementAndGet());

        this.itemName = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInventory(int inv) {
        this.inventory = inv;
    }

    public int getInventory() {
        return this.inventory;
    }

    public void decrementInventory() {
        if (this.inventory > 0) {
            this.inventory--;
        } else {
            this.inventory = 0;
        }
    }

    @Id
    public int getId() {
        return (this.id);
    }

    public void setItemName(String newName) {
        this.itemName = newName;
    }

    public String getItemName() {
        return (this.itemName);
    }

    @Override
    public String toString() {
        String toRet = "";

        toRet += this.itemName;

        return (toRet);
    }
}
