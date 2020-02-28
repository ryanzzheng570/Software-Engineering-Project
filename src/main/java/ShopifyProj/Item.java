package ShopifyProj;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Item {
    private int id;

    private String itemName;

    private static final AtomicLong counter = new AtomicLong();

    public Item(String name){
        this.id = Math.toIntExact(counter.incrementAndGet());

        this.itemName = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId(){
        return(this.id);
    }

    public void setItemName(String newName){
        this.itemName = newName;
    }

    public String getItemName(){
        return(this.itemName);
    }
}
