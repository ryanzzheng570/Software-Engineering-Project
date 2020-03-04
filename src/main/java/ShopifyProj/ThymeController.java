package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import javax.persistence.Id;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ThymeController {
    @Autowired
    private ShopRepository shopRepo;

    private List<Integer> getIds() {
        List<Integer> ids = new ArrayList<Integer>();
        for (Shop shop : shopRepo.findAll()) {
            Integer id = shop.getId();
            ids.add(id);
        }

        return ids;

    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("shopIds", getIds());

        return "index";
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
    public String addItem(@RequestParam (value = "shopID") Integer shopId, @RequestParam (value = "url") String url,
                          @RequestParam (value = "altText") String altText, @RequestParam (name = "name") String name,
                          @RequestParam (value = "cost") String cost,Model model){
        Optional<Shop> shop = shopRepo.findById(shopId);


        if (shop.isPresent()){
            Images image = new Images(url, altText);
            List<Images> imagesToAdd = new ArrayList<Images>();
            imagesToAdd.add(image);

            Item finalToAdd = new Item(name, imagesToAdd, cost);


            Shop finalShop = shop.get();
            finalShop.addItem(finalToAdd);
        }

        return displayYourShop(shopId, model);
    }

    @PostMapping("/removeItem")
    public String removeItem(@RequestParam (value = "shopID") Integer shopId, @RequestParam (value = "itemId") Integer itemId,
                             Model model){
        Optional<Shop> shop = shopRepo.findById(shopId);


        if (shop.isPresent()){
            Shop finalShop = shop.get();
            if (finalShop.getItem(itemId) != null){
                finalShop.removeItemWithId(itemId);
            }
        }

        return displayYourShop(shopId, model);
    }

}
