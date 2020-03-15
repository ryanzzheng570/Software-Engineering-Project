package ShopifyProj.Controller;

import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import ShopifyProj.Model.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ShopController {
    @Autowired
    private MerchantController merchCont;

    @Autowired
    private ShopRepository shopRepo;

    @GetMapping("/goToShopCustomerView")
    public String viewShopPageById(@RequestParam(value = "shopId") int aShopId, Model model) {
        Shop toShow = shopRepo.findById((aShopId));
        model.addAttribute("shop", toShow);
        System.out.println(toShow);
        return "CustomerShopViewPage";
    }

    @PostMapping("/changeShopName")
    public @ResponseBody Shop changeShopName(@RequestParam(value = "shopId") int shopId,
                                 @RequestParam(value = "shopName") String newName,
                                 Model model) {
        Shop checkShop = shopRepo.findById(shopId);
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        checkShop.setShopName(newName);

        shopRepo.save(checkShop);

        return checkShop;
    }

    @PostMapping("/removeTag")
    public @ResponseBody Shop removeTag(@RequestParam(value = "shopId") int shopId,
                                        @RequestParam(value = "tagId") int tagId,
                                        Model model) {
        Shop checkShop = shopRepo.findById(shopId);
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        checkShop.removeTagWithId(tagId);

        shopRepo.save(checkShop);

        return checkShop;
    }

    @PostMapping("/addTag")
    public @ResponseBody Tag addTag(@RequestParam(value = "shopId") int shopId,
                                     @RequestParam(value = "tagName") String tagName,
                                     Model model) {
        Shop checkShop = shopRepo.findById(shopId);
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        Tag toAdd = new Tag(tagName);
        checkShop.addTag(toAdd);

        shopRepo.save(checkShop);

        return toAdd;
    }

    @PostMapping("/addShop")
    public String addShop(@RequestParam(value = "shopName") String name, Model model) {
        Shop newShop = new Shop(name, Optional.empty());

        shopRepo.save(newShop);

        return displayYourShop(newShop.getId(), model);
    }

    @GetMapping("/goToEditShopPage")
    public String displayYourShop(@RequestParam(value = "shopId") int shopId, Model model){
        Shop theShop = shopRepo.findById(shopId);
        if (theShop == null){
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        model.addAttribute("shop", theShop);
        model.addAttribute("item", new Item());
        model.addAttribute("tag", new Tag());

        return "EditShopPage";
    }
}
