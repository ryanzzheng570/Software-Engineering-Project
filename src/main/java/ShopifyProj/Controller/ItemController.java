package ShopifyProj.Controller;

import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Repository.ShopRepository;
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
    private ShopRepository shopRepo;

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
    public @ResponseBody Item addItem(@RequestParam (value = "shopId") int shopId,
                                      @RequestParam (value = "url") String url,
                                      @RequestParam (value = "altText") String altText,
                                      @RequestParam (value = "itemName") String name,
                                      @RequestParam (value = "cost") String cost,
                                      @RequestParam (value = "inventory") int inventory,
                                      Model model){
        Item itemToAdd = null;

        Shop shop = shopRepo.findById(shopId);
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

            shop.addItem(itemToAdd);
            shopRepo.save(shop);
        }

        return itemToAdd;
    }

    @PostMapping("/removeItem")
    public @ResponseBody Shop removeItem(@RequestParam (value = "shopId") int shopId,
                                         @RequestParam (value = "itemId") int itemId,
                                         Model model){

        Shop checkShop = shopRepo.findById(shopId);
        if (checkShop != null){
            if (checkShop.getItem(itemId) != null){
                checkShop.removeItemWithId(itemId);
                shopRepo.save(checkShop);
            }
        }

        return checkShop;
    }

    @PostMapping("/editItem")
    public @ResponseBody Shop editItem(@RequestParam (value = "shopId") int shopId,
                                       @RequestParam (value = "itemId") int itemId,
                                       @RequestParam (value = "url") String url,
                                       @RequestParam (value = "altText") String altText,
                                       @RequestParam (value = "itemName") String itemName,
                                       @RequestParam (value = "cost") String cost,
                                       @RequestParam (value = "inventory") int inventory,
                                         Model model){

        Shop checkShop = shopRepo.findById(shopId);
        if (checkShop != null){
            if (checkShop.getItem(itemId) != null){
                checkShop.getItem(itemId).setInventory(inventory);
                checkShop.getItem(itemId).setUrl(0, url);
                checkShop.getItem(itemId).setAltText(0, altText);
                checkShop.getItem(itemId).setItemName(itemName);
                checkShop.getItem(itemId).setCost(cost);
                shopRepo.save(checkShop);
            }
        }

        return checkShop;
    }
}
