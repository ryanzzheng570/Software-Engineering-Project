package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


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

    private Shop getShopByName(String aShopName) {
        return shopRepo.findByShopName(aShopName);
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
    public String viewShopPage(@RequestParam(value = "shopName") String aShopName, Model model) {
        model.addAttribute("shop", getShopByName(aShopName));
        return "shopPage";
    }

}
