package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Shop {
    private int id;

    private String shopName;
    private List<String> tags;
    private List<Item> items;

    private static final AtomicLong counter = new AtomicLong();

    private static final String DEFAULT_SHOP_NAME = "";

    @Autowired
    public Shop()
    {
        this(DEFAULT_SHOP_NAME,
            Optional.of(new ArrayList<String>()),
            Optional.of(new ArrayList<Item>()));
    }

    public Shop(String shopName, Optional<List<String>> tags, Optional<List<Item>> items){
        this.id = Math.toIntExact(counter.incrementAndGet());

        this.shopName = shopName;

        if (tags.isPresent()){
            this.tags = tags.get();
        } else {
            this.tags = new ArrayList<String>();
        }

        if (items.isPresent()){
            this.items = items.get();
        } else {
            this.items = new ArrayList<Item>();
        }
    }

    @Id
    public int getId() {
        return this.id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void addItem(Item newItem) {
        this.items.add(newItem);
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public List<Item> getItems(){
        return(this.items);
    }

    public void setItems(List<Item> newItemLst) {
        this.items = newItemLst;
    }

    public void clearItems(){
        this.items = new ArrayList<Item>();
    }

    public Item getItem(int ind) {
        Item toRet = null;

        if (ind < this.items.size()) {
            toRet = this.items.get(ind);
        }

        return(toRet);
    }

    public void removeItem(int ind){
        if (ind < this.items.size()) {
            this.items.remove(ind);
        }
    }

    public void removeItemWithId(int id) {
        int ind = 0;
        while (ind < this.items.size()){
            if (this.items.get(ind).getId() == id) {
                break;
            }
            ind += 1;
        }
        this.removeItem(ind);
    }

    public void addTag(String newTag) {
        this.tags.add(newTag);
    }

    public List<String> getTags(){
        return(this.tags);
    }

    public void setTags(List<String> newTagList) {
        this.tags = newTagList;
    }

    public void clearTags(){
        this.tags = new ArrayList<String>();
    }

    public String getTag(int ind) {
        String toRet = null;

        if (ind < this.tags.size()) {
            toRet = this.tags.get(ind);
        }

        return(toRet);
    }

    public void removeTag(int ind){
        if (ind < this.tags.size()) {
            this.tags.remove(ind);
        }
    }

    @Override
    public String toString() {
        String toRet = "";
        toRet += String.format("Shop (Id: %d): \n", this.id);

        toRet += "Tags: [";
        for (String tag: this.tags) {
            toRet += tag;
            toRet += ", ";
        }
        toRet = toRet.substring(0, toRet.length() - 2);

        for (Item item : this.items) {
            toRet += "\tItem: " + item.toString() + "\n";
        }

        return(toRet);
    }
}
