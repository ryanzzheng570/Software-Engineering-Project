package ShopifyProj.Controller;

import ShopifyProj.Model.Merchant;
import ShopifyProj.Model.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MerchantController {
    @Autowired
    private ShopController shopCont;

    @GetMapping("/addNewMerchant")
    public String viewAddNewMerchantPage(Model model) {
        model.addAttribute("merchant", new Merchant());
        return "addMerchantPage";
    }

    @PostMapping("/addNewMerchant")
    public @ResponseBody
    Merchant addNewMerchant(@ModelAttribute Merchant newMerchant, Model model) {
        //TODO: FIX
        return newMerchant;
    }

    private List<Shop> getShops() {
        //TODO: FIX
        List<Shop> shops = new ArrayList<Shop>();
//        for (Shop shop : shopRepo.findAll()) {
//            shops.add(shop);
//        }

        return shops;
    }

    @GetMapping("/goToAddShopPage")
    public String viewAddShopPage(Model model) {
        model.addAttribute("shops", getShops());
        model.addAttribute("shop", new Shop());

        return "CreateShopPage";
    }

    @GetMapping("/goToMerchantMenuPage")
    public String viewMerchantMenuPage(Model model) {
        model.addAttribute("shops", getShops());
        model.addAttribute("shop", new Shop());

        return "MerchantMenuPage";
    }

    @PostMapping("/deleteShop")
    public @ResponseBody Boolean deleteShop(@RequestParam(value = "shopId") int shopId, Model model) {
        // TODO: FIX
//        shopRepo.deleteById(shopId);

        return getShops().isEmpty();
    }
}
