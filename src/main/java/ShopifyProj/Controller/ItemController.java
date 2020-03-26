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

    @Autowired
    private ShopController shopCont;

    @PostMapping("/addItem")
    public @ResponseBody Item addItem(@RequestParam (value = "shopId") String shopId,
                                      @RequestParam (value = "setId") String itemId,
                                      @RequestParam (value = "url") String url,
                                      @RequestParam (value = "altText") String altText,
                                      @RequestParam (value = "itemName") String name,
                                      @RequestParam (value = "cost") Double cost,
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

            itemToAdd = new Item(name, imageToAdd, cost, inventory);
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

    @PostMapping("/editItem")
    public @ResponseBody Shop editItem(@RequestParam (value = "shopId") String shopId,
                                       @RequestParam (value = "itemId") String itemId,
                                       @RequestParam (value = "url") String url,
                                       @RequestParam (value = "altText") String altText,
                                       @RequestParam (value = "itemName") String itemName,
                                       @RequestParam (value = "cost") Double cost,
                                       @RequestParam (value = "inventory") int inventory,
                                         Model model){

        Shop checkShop = null;
        try {
            checkShop = FirebaseController.getShopWithId(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkShop != null){
            if (checkShop.getItem(itemId) != null){
                checkShop.getItem(itemId).setInventory(inventory);

                if ((checkShop.getItem(itemId).getImages().size() > 0) && (!url.equals(""))) {
                    checkShop.getItem(itemId).setUrl(0, url);
                    checkShop.getItem(itemId).setAltText(0, altText);
                } else if ((checkShop.getItem(itemId).getImages().size() > 0) && (url.equals(""))) {
                    checkShop.getItem(itemId).setImages(new ArrayList<Image>());
                }

                checkShop.getItem(itemId).setItemName(itemName);
                checkShop.getItem(itemId).setCost(cost);
            }
        }

        return checkShop;
    }
}
