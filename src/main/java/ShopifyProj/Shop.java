package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Shop {
    private int id;

    private String shopName;

    private Set<Tag> tags;

    private Set<Item> items;

    private static final AtomicLong counter = new AtomicLong();

    private static final String DEFAULT_SHOP_NAME = "";

    @Autowired
    public Shop()
    {
        this(DEFAULT_SHOP_NAME,
            Optional.of(new HashSet<>()),
            Optional.of(new HashSet<Item>()));
    }

    public Shop(String shopName, Optional<Set<Tag>> tags, Optional<Set<Item>> items){
        this.id = Math.toIntExact(counter.incrementAndGet());

        this.shopName = shopName;

        if (tags.isPresent()){
            this.tags = tags.get();
        } else {
            this.tags = new HashSet<>();
        }

        if (items.isPresent()){
            this.items = items.get();
        } else {
            this.items = new HashSet<Item>();
        }
    }

    @Id
    public int getId() {
        return this.id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void addItem(Item newItem) {
        this.items.add(newItem);
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public Set<Item> getItems(){
        return(this.items);
    }

    public void setItems(Set<Item> newItemLst) {
        this.items = newItemLst;
    }

    public void clearItems(){
        this.items = new HashSet<Item>();
    }

    public Item getItem(int id) {
        for (Item item : this.items) {
            if (item.getId() == id) {
                return(item);
            }
        }
        return(null);
    }

    public void removeItemWithId(int id) {
        Item toRemove = null;
        for (Item item : this.items) {
            if (item.getId() == id) {
                toRemove = item;
                break;
            }
        }
        this.items.remove(toRemove);
    }

    public void addTag(Tag newTag) {
        this.tags.add(newTag);
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public Set<Tag> getTags(){
        return(this.tags);
    }

    public void setTags(Set<Tag> newTagList) {
        this.tags = newTagList;
    }

    public void clearTags(){
        this.tags = new HashSet<Tag>();
    }

    public Tag getTag(int id) {
        for (Tag tag : this.tags) {
            if (tag.getId() == id) {
                return (tag);
            }
        }

        return(null);
    }

    public void removeTagWithId(int id) {
        Tag toRemove = null;
        for (Tag tag : this.tags) {
            if (tag.getId() == id) {
                toRemove = tag;
                break;
            }
        }
        this.tags.remove(toRemove);
    }

    @Override
    public String toString() {
        String toRet = "";
        toRet += String.format("Shop (Id: %d): \n", this.id);

        toRet += "Tags: [";
        for (Tag tag: this.tags) {
            toRet += tag.toString();
            toRet += ", ";
        }
        toRet = toRet.substring(0, toRet.length() - 2);

        for (Item item : this.items) {
            toRet += "\tItem: " + item.toString() + "\n";
        }

        return(toRet);
    }
}
