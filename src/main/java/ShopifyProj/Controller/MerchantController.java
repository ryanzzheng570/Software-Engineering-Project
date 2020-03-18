package ShopifyProj.Controller;

import ShopifyProj.Model.Merchant;
import ShopifyProj.Model.Shop;
import ShopifyProj.Model.Tag;
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
        return "CreateMerchantAccountPage";
    }

    @PostMapping("/addNewMerchant")
    public @ResponseBody Merchant addNewMerchant(@RequestParam(value = "userName") String userName,
                                                 @RequestParam(value = "password") String password,
                                                 @RequestParam(value = "email") String email,
                                                 @RequestParam(value = "contactPhoneNumber") String phoneNum,
                                                 @RequestParam(value = "setId") String newId,
                                                 Model model) {

        Merchant toAdd = new Merchant(userName, phoneNum, email, password);
        toAdd.setId(newId);
        // TODO: FIX
        //merchantRepository.save(merchant);
        /*
            todo: Should navigate to profile page, redirect to home page for now
         */
        return toAdd;
    }

    private List<Shop> getShops() {
        return FirebaseController.getCurrShops();
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
    public @ResponseBody Boolean deleteShop(@RequestParam(value = "shopId") String shopId, Model model) {
        ArrayList<Shop> currShops = FirebaseController.getCurrShops();

        int indToRemove = -1;
        for (int i = 0; i < currShops.size(); i++){
            if (currShops.get(i).getId().equals(shopId)) {
                indToRemove = i;
                i = currShops.size() + 2;
            }
        }

        currShops.remove(indToRemove);

        return getShops().isEmpty();
    }
}
