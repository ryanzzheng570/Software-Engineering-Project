package ShopifyProj.Controller;

import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Model.TempShop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import ShopifyProj.Model.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;


@Controller
public class ShopController {
    @Autowired
    private MerchantController merchCont;

    ArrayList<Shop> currShops = FirebaseController.getCurrShops();

    @GetMapping("/goToShopCustomerView")
    public String viewShopPageById(@RequestParam(value = "shopId") String aShopId, Model model) {
        // Until database sorted out
        String aShopId2 = "-M2QECi8-MSD1yp8jzA9";
        model.addAttribute("shopID", aShopId2);
        TempShop shop = FirebaseController.getShopFromID(aShopId2);
        model.addAttribute("shop", FirebaseController.getShopFromID(aShopId2));
        return "CustomerShopViewPage";
    }

    @PostMapping("/updateShopId")
    public @ResponseBody Shop changeShopId(@RequestParam(value = "oldId") String oldId,
                                           @RequestParam(value = "newId") String newId,
                                           Model model) {
        Shop checkShop = FirebaseController.getShopWithId(oldId);
        checkShop.setId(newId);

        return checkShop;
    }

    @PostMapping("/changeShopName")
    public @ResponseBody Shop changeShopName(@RequestParam(value = "shopId") String shopId,
                                 @RequestParam(value = "shopName") String newName,
                                 Model model) {
        Shop checkShop = FirebaseController.getShopWithId(shopId);

        checkShop.setShopName(newName);

        return checkShop;
    }

    @PostMapping("/removeTag")
    public @ResponseBody Shop removeTag(@RequestParam(value = "shopId") String shopId,
                                        @RequestParam(value = "tagId") String tagId,
                                        Model model) {
        Shop checkShop = FirebaseController.getShopWithId(shopId);

        checkShop.removeTagWithId(tagId);

        return checkShop;
    }

    @PostMapping("/addTag")
    public @ResponseBody Tag addTag(@RequestParam(value = "shopId") String shopId,
                                     @RequestParam(value = "tagName") String tagName,
                                    @RequestParam(value = "setId") String tagId,
                                     Model model) {
        Shop checkShop = FirebaseController.getShopWithId(shopId);

        Tag toAdd = new Tag(tagName);
        toAdd.setId(tagId);
        checkShop.addTag(toAdd);

        return toAdd;
    }

    @PostMapping("/addShop")
    public @ResponseBody Shop addShop(@RequestParam(value = "shopName") String name,
                                      @RequestParam(value = "setId") String newId,
                                      Model model) {
        Shop newShop = new Shop(name, Optional.empty());
        newShop.setId(newId);

        currShops.add(newShop);

        return newShop;
    }

    @GetMapping("/goToEditShopPage")
    public String displayYourShop(@RequestParam(value = "shopId") String shopId, Model model){
        Shop checkShop = FirebaseController.getShopWithId(shopId);

        model.addAttribute("shop", checkShop);
        model.addAttribute("item", new Item());
        model.addAttribute("tag", new Tag());

        return "EditShopPage";
    }
}
