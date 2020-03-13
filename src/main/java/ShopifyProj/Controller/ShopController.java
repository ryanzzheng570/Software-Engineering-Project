package ShopifyProj.Controller;

import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Repository.MerchantRepository;
import ShopifyProj.Repository.ShopRepository;
import ShopifyProj.Model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;

@Controller
public class ShopController {
    @Autowired
    private MerchantController merchCont;

    @Autowired
    private ShopRepository shopRepo;

    public Shop getShopById(int aShopId) {
        return shopRepo.findById(aShopId);
    }

    @GetMapping("/goToShopCustomerView")
    public String viewShopPageById(@RequestParam(value = "shopId") int aShopId, Model model) {
        model.addAttribute("shop", getShopById(aShopId));
        return "CustomerShopViewPage";
    }

    @PostMapping("/changeShopName")
    public String changeShopName(@RequestParam(value = "shopId") int shopId,
                                 @RequestParam(value = "shopName") String newName,
                                 Model model) {
        Shop shopToEdit = null;

        Shop checkShop = shopRepo.findById(shopId);
        if (checkShop != null) {
            shopToEdit = checkShop;
        } else {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        shopToEdit.setShopName(newName);

        shopRepo.save(shopToEdit);

        return displayYourShop(shopId, model);
    }

    @PostMapping("/removeTag")
    public String removeTag(@RequestParam(value = "shopId") int shopId,
                           @RequestParam(value = "tagId") int tagId,
                           Model model) {
        Shop shopToEdit = null;

        Shop checkShop = shopRepo.findById(shopId);
        if (checkShop != null) {
            shopToEdit = checkShop;
        } else {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        shopToEdit.removeTagWithId(tagId);

        shopRepo.save(shopToEdit);

        return displayYourShop(shopId, model);
    }

    @PostMapping("/addTag")
    public String removeTag(@RequestParam(value = "shopId") int shopId,
                            @RequestParam(value = "tagName") String tagName,
                            Model model) {
        Shop shopToEdit = null;

        Shop checkShop = shopRepo.findById(shopId);
        if (checkShop != null) {
            shopToEdit = checkShop;
        } else {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        shopToEdit.addTag(new Tag(tagName));

        shopRepo.save(shopToEdit);

        return displayYourShop(shopId, model);
    }

    @PostMapping("/deleteShop")
    public String addShop(@RequestParam(value = "shopId") int shopId, Model model) {
        shopRepo.deleteById(shopId);

        return merchCont.viewMerchantMenuPage(model);
    }

    @PostMapping("/addShop")
    public String addShop(@RequestParam(value = "shopName") String name, Model model) {
        Shop newShop = new Shop(name, Optional.empty());

        shopRepo.save(newShop);

        return displayYourShop(newShop.getId(), model);
    }

    @GetMapping("/goToShopMerchantView")
    public String displayYourShop(@RequestParam(value = "shopId") Integer shopId, Model model){

        Shop toView = null;

        Optional<Shop> theShop = shopRepo.findById(shopId);
        if(theShop.isPresent()){
            toView = theShop.get();
            System.out.println(toView);
        }else {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        model.addAttribute("shop", toView);
        model.addAttribute("item", new Item());
        model.addAttribute("tag", new Tag());

        return "EditShopPage";
    }
}
