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
        return "CreateMerchantAccountPage";
    }

    @PostMapping("/addNewMerchant")
    public String addNewMerchant(@ModelAttribute Merchant merchant, Model model) {
        merchantRepository.save(merchant);
        /*
            todo: Should navigate to profile page, redirect to home page for now
         */
        return "redirect:/";
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

    @PostMapping("/deleteShop")
    public @ResponseBody Boolean deleteShop(@RequestParam(value = "shopId") int shopId, Model model) {
        shopRepo.deleteById(shopId);

        return getShops().isEmpty();
    }
}
