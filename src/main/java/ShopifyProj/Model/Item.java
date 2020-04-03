package ShopifyProj.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Item {

    private String id;

    private String itemName;

    private int inventory;

    private List<Image> images;
    private Double cost;

    private String storeName;
    private String storeID;

    private static final AtomicLong counter = new AtomicLong();

    @Autowired
    public Item() {
        this("", new ArrayList<Image>(), 0.0, 0);
    }

    public Item(String name, List<Image> images, Double cost, int inventory){
        this.id = Integer.toString(Math.toIntExact(counter.incrementAndGet()));

        this.itemName = name;
        this.images = images;
        this.inventory = inventory;
        this.cost = cost;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInventory(int inv) {
        this.inventory = inv;
    }

    public int getInventory() {
        return this.inventory;
    }

    public void reduceQuantity(int amount) {
        this.inventory -= amount;
    }

    public void decrementInventory() {
        if (this.inventory > 0) {
            this.inventory--;
        } else {
            this.inventory = 0;
        }
    }

    @Id
    public String getId() {
        return (this.id);
    }

    public void setItemName(String newName) {
        this.itemName = newName;
    }

    public String getItemName() {
        return (this.itemName);
    }


    public void setImages(List<Image> newImages){ this.images = newImages; }

    public void setUrl(int imageNum, String newUrl) {
        this.images.get(imageNum).setUrl(newUrl);
    }

    public void setAltText(int imageNum, String newAltText) { this.images.get(imageNum).setAltText(newAltText); }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public List<Image> getImages() {return images;}

    public String getUrl(int imageNum) {return this.images.get(imageNum).getUrl(); }

    public String getAltText(int imageNum) {return images.get(imageNum).getAltText(); }


    public void setCost(Double newCost){
        this.cost = newCost;
    }

    public Double getCost(){ return this.cost; }


    @Override
    public String toString() {
        String toRet = "";

        toRet += this.itemName + "\n";
        for (Image image : this.images) {
            toRet += "Image: " + image.toString() + "\n";
        }

        return (toRet);
    }
}
