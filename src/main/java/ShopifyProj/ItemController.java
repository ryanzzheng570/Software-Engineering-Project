package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import javax.swing.text.html.Option;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ItemController {
    @Autowired
    private ShopRepository shopRepo;

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

        System.out.println(shopId);
        System.out.println(url);
        System.out.println(altText);
        System.out.println(name);
        System.out.println(cost);
        System.out.println(inventory);


        if (shop.isPresent()){
            List<Images> imagesToAdd = new ArrayList<Images>();

            if (!url.equals("")) {
                Images image = new Images(url, altText);
                imagesToAdd.add(image);
                System.out.println("ADDING IMAGE" + image.toString());
            }

            String newCost = parseCostInput(cost);

            Item finalItemToAdd = new Item(name, imagesToAdd, newCost, inventory);

            Shop finalShop = shop.get();
            finalShop.addItem(finalItemToAdd);
            shopRepo.save(finalShop);
        }

        ShopController temp = new ShopController();
        return temp.displayYourShop(shopId, model);
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

        ShopController temp = new ShopController();
        return temp.displayYourShop(shopId, model);
    }

}
