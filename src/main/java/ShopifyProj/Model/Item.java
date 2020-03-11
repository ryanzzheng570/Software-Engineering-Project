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
    private int id;

    private String itemName;

    private int inventory;

    private List<Image> images;
    private String cost;

    private static final AtomicLong counter = new AtomicLong();

    @Autowired
    public Item() {
        this("", new ArrayList<Image>(), "", 0);
    }

    public Item(String name, List<Image> images, String cost, int inventory){
        this.id = Math.toIntExact(counter.incrementAndGet());

        this.itemName = name;
        this.images = images;
        this.inventory = inventory;
        this.cost = cost;
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


    public void setImages(List<Image> newImages){ this.images = newImages; }

    public void setUrl(int imageNum, String newUrl) {
        this.images.get(imageNum).setUrl(newUrl);
    }

    public void setAltText(int imageNum, String newAltText) { this.images.get(imageNum).setAltText(newAltText); }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public List<Image> getImages() {return images;}

    public String getUrl(int imageNum) {return this.images.get(imageNum).getUrl(); }

    public String getAltText(int imageNum) {return images.get(imageNum).getAltText(); }


    public void setCost(String newCost){
        this.cost = newCost;
    }

    public String getCost(){ return this.cost; }


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
