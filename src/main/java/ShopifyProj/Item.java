package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Item {
    private int id;

    private String itemName;

    private static final AtomicLong counter = new AtomicLong();

    @Autowired
    public Item() {
        this("");
    }

    public Item(String name){
        this.id = Math.toIntExact(counter.incrementAndGet());

        this.itemName = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    public int getId(){
        return(this.id);
    }

    public void setItemName(String newName){
        this.itemName = newName;
    }

    public String getItemName(){
        return(this.itemName);
    }

    @Override
    public String toString() {
        String toRet = "";

        toRet += this.itemName;

        return(toRet);
    }
}
