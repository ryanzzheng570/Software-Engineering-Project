package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Id;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ThymeController {
    @Autowired
    private ShopRepository shopRepo;

    private List<Shop> getShops() {
        List<Shop> shops = new ArrayList<Shop>();
        for (Shop shop : shopRepo.findAll()) {
            shops.add(shop);
        }

        return shops;

    }

    private Shop getShopById(int aShopId) {
        return shopRepo.findById(aShopId);
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return "homepage";
    }

    @GetMapping("/goToAddShopPage")
    public String viewAddShopPage(Model model) {
        model.addAttribute("shops", getShops());
        model.addAttribute("shop", new Shop());

        return "addShopPage";
    }


    @GetMapping("/goToShop")
    public String viewShopPageById(@RequestParam(value = "shopId") int aShopId, Model model) {
        model.addAttribute("shop", getShopById(aShopId));
        return "shopPage";
    }

    @GetMapping("/YourShopPage")
    public String displayYourShop(@RequestParam (value = "shopId") Integer shopId, Model model){

        Shop toView = null;

        Optional<Shop> theShop = shopRepo.findById(shopId);
        if(theShop.isPresent()){
            toView = theShop.get();
        }else {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        model.addAttribute("shop", toView);
        model.addAttribute("item", new Item());


        return "MerchantShopPage";
    }

    @PostMapping("/addItem")
    public String addItem(@RequestParam (value = "shopId") Integer shopId, @RequestParam (value = "url") String url,
                          @RequestParam (value = "altText") String altText, @RequestParam (value = "itemName") String name,
                          @RequestParam (value = "cost") String cost, @RequestParam (value = "inventory") int inventory,Model model){
        Optional<Shop> shop = shopRepo.findById(shopId);


        if (shop.isPresent()){
           // System.out.println(url + "\n" + altText);
            Images image = new Images(url, altText);
            List<Images> imagesToAdd = new ArrayList<Images>();
            imagesToAdd.add(image);

            Item finalItemToAdd = new Item(name, imagesToAdd, cost, inventory);

            Shop finalShop = shop.get();
            finalShop.addItem(finalItemToAdd);
            //System.out.println(finalShop.getItems().get(0).getImages().get(0).getUrl());
            shopRepo.save(finalShop);
        }


        return displayYourShop(shopId, model);
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

        return displayYourShop(shopId, model);
    }

}
