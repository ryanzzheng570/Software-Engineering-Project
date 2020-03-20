package ShopifyProj.Controller;

import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import ShopifyProj.Model.Image;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ItemController {
    private static final int MAX_URL_LEN = 255;

    ArrayList<Shop> currShops = FirebaseController.getCurrShops();

    @Autowired
    private ShopController shopCont;

    private String parseCostInput(String cost) {
        cost = cost.replaceAll("[$]", "");

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        boolean numeric = true;
        double check = 0;

        try {
            check = Double.parseDouble(cost);
        } catch (NumberFormatException e) {
            numeric = false;
        }

        if (numeric){
            cost = formatter.format(check);
        }else{
            cost = "Invalid Cost Input";
        }
        return(cost);
    }


    @PostMapping("/addItem")
    public @ResponseBody Item addItem(@RequestParam (value = "shopId") String shopId,
                                      @RequestParam (value = "setId") String itemId,
                                      @RequestParam (value = "url") String url,
                                      @RequestParam (value = "altText") String altText,
                                      @RequestParam (value = "itemName") String name,
                                      @RequestParam (value = "cost") String cost,
                                      @RequestParam (value = "inventory") int inventory,
                                      Model model){
        Item itemToAdd = null;

        Shop shop = null;
        try {
            shop = FirebaseController.getShopWithId(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (shop != null){
            List<Image> imageToAdd = new ArrayList<Image>();

            if (!url.equals("")) {
                if (url.length() > MAX_URL_LEN) {
                    System.out.println("ERROR: Cannot save image. URL too long.");
                } else {
                    Image image = new Image(url, altText);
                    imageToAdd.add(image);
                }
            }

            String newCost = parseCostInput(cost);

            itemToAdd = new Item(name, imageToAdd, newCost, inventory);
            itemToAdd.setId(itemId);

            shop.addItem(itemToAdd);
        }

        return itemToAdd;
    }

    @PostMapping("/removeItem")
    public @ResponseBody Shop removeItem(@RequestParam (value = "shopId") String shopId,
                                         @RequestParam (value = "itemId") String itemId,
                                         Model model){

        Shop checkShop = null;
        try {
            checkShop = FirebaseController.getShopWithId(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkShop.removeItemWithId(itemId);

        return checkShop;
    }

}
