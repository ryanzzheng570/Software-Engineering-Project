package ShopifyProj.Controller;

import ShopifyProj.Model.Image;
import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        System.out.println("HERE");
        cost = cost.replaceAll("[$]", "");

        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        boolean numeric = true;
        double check = 0;

        try {
            check = Double.parseDouble(cost);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        System.out.println(numeric);

        if (numeric){
            cost = formatter.format(check);
        }else{
            cost = "Invalid Cost Input";
        }
        System.out.println("DONE");
        return(cost);
    }


    @PostMapping("/addItem")
    public String addItem(@RequestParam (value = "shopId") Integer shopId, @RequestParam (value = "url") String url,
                          @RequestParam (value = "altText") String altText, @RequestParam (value = "itemName") String name,
                          @RequestParam (value = "cost") String cost, @RequestParam (value = "inventory") int inventory, Model model){
        Optional<Shop> shop = shopRepo.findById(shopId);

        if (shop.isPresent()){
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

            Item finalItemToAdd = new Item(name, imageToAdd, newCost, inventory);

            Shop finalShop = shop.get();
            finalShop.addItem(finalItemToAdd);
            shopRepo.save(finalShop);
        }

        return shopCont.displayYourShop(shopId, model);
    }

    @PostMapping("/removeItem")
    public String removeItem(@RequestParam (value = "shopId") Integer shopId, @RequestParam (value = "itemId") Integer itemId,
                             Model model){
        Optional<Shop> shop = shopRepo.findById(shopId);

        if (shop.isPresent()){
            Shop finalShop = shop.get();
            System.out.println(itemId + " Id before getting item");
            if (finalShop.getItem(itemId) != null){
                System.out.println(finalShop.getItem(itemId).getId() + " Id after getting item");
                finalShop.removeItemWithId(itemId);
                shopRepo.save(finalShop);
            }
        }

        return shopCont.displayYourShop(shopId, model);
    }

}
