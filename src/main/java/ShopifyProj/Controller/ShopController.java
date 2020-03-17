package ShopifyProj.Controller;

import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Model.TempShop;

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

    @GetMapping("/goToShopCustomerView")
    public String viewShopPageById(@RequestParam(value = "shopId") int aShopId, Model model) {
        // Until database sorted out
        String aShopId2 = "-M2QECi8-MSD1yp8jzA9";
        model.addAttribute("shopID", aShopId2);
        TempShop shop = FirebaseController.getShopFromID(aShopId2);
        model.addAttribute("shop", FirebaseController.getShopFromID(aShopId2));
        return "CustomerShopViewPage";
    }

    @PostMapping("/updateShopId")
    public @ResponseBody Shop changeShopId(@RequestParam(value = "newId") int shopId,
                                             Model model) {
        //TODO: FIX
        Shop checkShop = new Shop();
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        checkShop.setId(shopId);

        return checkShop;
    }

    @PostMapping("/changeShopName")
    public @ResponseBody Shop changeShopName(@RequestParam(value = "shopId") int shopId,
                                 @RequestParam(value = "shopName") String newName,
                                 Model model) {
        // TODO: FIX
        Shop checkShop = new Shop();
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        checkShop.setShopName(newName);

        return checkShop;
    }

    @PostMapping("/removeTag")
    public @ResponseBody Shop removeTag(@RequestParam(value = "shopId") int shopId,
                                        @RequestParam(value = "tagId") int tagId,
                                        Model model) {
        // TODO: FIX
        Shop checkShop = new Shop();
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        checkShop.removeTagWithId(tagId);

        return checkShop;
    }

    @PostMapping("/addTag")
    public @ResponseBody Tag addTag(@RequestParam(value = "shopId") int shopId,
                                     @RequestParam(value = "tagName") String tagName,
                                     Model model) {

        //TODO: FIX
        Shop checkShop = new Shop();
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        Tag toAdd = new Tag(tagName);
        checkShop.addTag(toAdd);

        return toAdd;
    }

    @PostMapping("/addShop")
    public @ResponseBody Shop addShop(@RequestParam(value = "shopName") String name, Model model) {
        //TODO: FIX
        Shop newShop = new Shop(name, Optional.empty());

        return newShop;
    }

    @GetMapping("/goToEditShopPage")
    public String displayYourShop(@RequestParam(value = "shopId") int shopId, Model model){
        //TODO: FIX
        Shop theShop = new Shop();
        if (theShop == null){
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        model.addAttribute("shop", theShop);
        model.addAttribute("item", new Item());
        model.addAttribute("tag", new Tag());

        return "EditShopPage";
    }
}
