package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class Shop {
    private int id;

    private String shopName;

    private Set<Tag> tags;

    private List<Item> items;

    private static final AtomicLong counter = new AtomicLong();

    private static final String DEFAULT_SHOP_NAME = "";

    @Autowired
    public Shop() {
        this(DEFAULT_SHOP_NAME,
                Optional.of(new HashSet<>()));
    }

    public Shop(String shopName, Optional<Set<Tag>> tags) {
        this.id = Math.toIntExact(counter.incrementAndGet());

        this.shopName = shopName;

        if (tags.isPresent()) {
            this.tags = tags.get();
        } else {
            this.tags = new HashSet<>();
        }

        this.items = new ArrayList<Item>();
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
    public List<Item> getItems() {
        return (this.items);
    }

    public void setItems(List<Item> newItemLst) {
        this.items = newItemLst;
    }

    public void clearItems() { this.items = new ArrayList<Item>(); }

    public Item getItem(int id) {
        for (Item item : this.items) {
            if (item.getId() == id) {
                return (item);
            }
        }
        return (null);
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
    public Set<Tag> getTags() {
        return (this.tags);
    }

    public void setTags(Set<Tag> newTagList) {
        this.tags = newTagList;
    }

    public void clearTags() {
        this.tags = new HashSet<Tag>();
    }

    public Tag getTag(int id) {
        for (Tag tag : this.tags) {
            if (tag.getId() == id) {
                return (tag);
            }
        }

        return (null);
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
        toRet += String.format("Shop Name: %s, Id: %d: \n", this.shopName, this.id);

        if (!this.tags.isEmpty()) {
            toRet += "Tags: [";
            for (Tag tag : this.tags) {
                toRet += tag.toString();
                toRet += ", ";
            }
            toRet = toRet.substring(0, toRet.length() - 2);
            toRet += "]\n";
        }

        for (Item item : this.items) {
            toRet += "\tItem: " + item.toString() + "\n";
        }

        return (toRet);
    }
}
