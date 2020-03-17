package ShopifyProj.Model;

public class TempItem {
    public String name;
    public String altText;
    public int inventory;
    public double cost;
    public String url;

    public TempItem() {
    }

    public TempItem(String name, String altText, int inventory, double cost, String url) {
        this.altText = altText;
        this.name = name;
        this.inventory = inventory;
        this.cost = cost;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TempItem{" +
                "name='" + name + '\'' +
                ", inventory=" + inventory +
                ", cost=" + cost +
                ", url='" + url + '\'' +
                '}';
    }
}