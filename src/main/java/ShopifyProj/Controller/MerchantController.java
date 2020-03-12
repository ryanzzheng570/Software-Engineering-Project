package ShopifyProj.Controller;

import ShopifyProj.Model.Merchant;
import ShopifyProj.Model.Shop;
import ShopifyProj.Repository.MerchantRepository;
import ShopifyProj.Repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MerchantController {
    @Autowired
    private ShopRepository shopRepo;

    @Autowired
    private ShopController shopCont;

    @Autowired
    private MerchantRepository merchantRepository;

    @GetMapping("/addNewMerchant")
    public String viewAddNewMerchantPage(Model model) {
        model.addAttribute("merchant", new Merchant());
        return "addMerchantPage";
    }

    @PostMapping("/addNewMerchant")
    public @ResponseBody
    Merchant addNewMerchant(@ModelAttribute Merchant newMerchant, Model model) {
        merchantRepository.save(newMerchant);
        return newMerchant;
    }

    private List<Shop> getShops() {
        List<Shop> shops = new ArrayList<Shop>();
        for (Shop shop : shopRepo.findAll()) {
            shops.add(shop);
        }

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

    @GetMapping("/goToEditShopPage")
    public String viewEditShopPage(@RequestParam(value = "shopId") int aShopId, Model model) {
        return shopCont.displayYourShop(aShopId, model);
    }
}
