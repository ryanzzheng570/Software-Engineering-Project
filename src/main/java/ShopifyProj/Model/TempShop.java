package ShopifyProj.Model;

import java.util.Map;

public class TempShop {
    public String name;
    public Map<String,TempItem> item;
    public Map<String, String> tag;

    public TempShop() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, TempItem> getItems() {
        return item;
    }

    public TempItem getItemByKey(String id) { return item.get(id); }

    public String getItemIDByName(String name) {
        for (Map.Entry<String,TempItem> item : item.entrySet()) {
            if (item.getValue().getName().equals(name)) {
                System.out.println("Found ID: " + item.getKey());
                return item.getKey();
            }
        }
        return null;
    }

    public void setItem(Map<String, TempItem> item) {
        this.item = item;
    }

    public Map<String, String> getTag() {
        return tag;
    }

    public void setTag(Map<String, String> tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TempShop{" +
                "name='" + name + '\'' +
                ", item=" + item +
                ", tag=" + tag +
                '}';
    }
}
