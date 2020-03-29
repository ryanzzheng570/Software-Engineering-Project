package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
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
        String username = "";
        boolean isCustomer = false;
        if(FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);

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

    @GetMapping("/goToAddShopPage")
    public String viewAddShopPage(Model model) {
        String username = "";
        boolean isCustomer = false;
        if(FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);

        if (FirebaseController.getCurrUser() == null ||(FirebaseController.getCurrUser() != null && FirebaseController.getCurrUser() instanceof Customer) ) {
            return "MerchantLoginPage";
        } else {
            model.addAttribute("shops", FirebaseController.getCurrUsersShops());
            model.addAttribute("shop", new Shop());
            model.addAttribute("currUser", FirebaseController.getCurrUser());
            model.addAttribute("isCustomer", FirebaseController.isCurrUserCustomer());

            return "CreateShopPage";
        }
    }

    @GetMapping("/goToMerchantMenuPage")
    public String viewMerchantMenuPage(Model model) {
        String username = "";
        boolean isCustomer = false;
        if(FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);

        if (FirebaseController.getCurrUser() == null || (FirebaseController.getCurrUser() != null && FirebaseController.getCurrUser() instanceof Customer)) {
            return "MerchantLoginPage";
        } else {
            model.addAttribute("shops", FirebaseController.getCurrUsersShops());
            model.addAttribute("shop", new Shop());
            model.addAttribute("currUser", FirebaseController.getCurrUser());
            model.addAttribute("isCustomer", FirebaseController.isCurrUserCustomer());

            return "MerchantMenuPage";
        }
    }

    @PostMapping("/deleteShop")
    public @ResponseBody Boolean deleteShop(@RequestParam(value = "shopId") String shopId, Model model) {
        ArrayList<Shop> currShops = FirebaseController.getDbShops();

        int indToRemove = -1;
        for (int i = 0; i < currShops.size(); i++){
            if (currShops.get(i).getId().equals(shopId)) {
                indToRemove = i;
                i = currShops.size() + 2;
            }
        }

        currShops.remove(indToRemove);

        ((Merchant) FirebaseController.getCurrUser()).removeShopById(shopId);

        return FirebaseController.getCurrUsersShops().isEmpty();
    }
}
