package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Merchant;
import ShopifyProj.Model.Shop;
import ShopifyProj.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String viewLoginPage(Model model) {
        return "Login";
    }

    @GetMapping("/loginAsMerchant")
    public String viewMerchantLogin(Model model) {
        model.addAttribute("user", new Merchant());
        model.addAttribute("isLoginFailed", false);
        return "MerchantLoginPage";
    }

    @GetMapping("/loginAsCustomer")
    public String viewCustomerLogin(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("isLoginFailed", false);
        return "CustomerLoginPage";
    }

    @PostMapping("/loginAsMerchant")
    public @ResponseBody Merchant signInAsMerchant(@RequestParam(value = "id") String userId,
                                                   @RequestParam(value = "userName") String name,
                                                   @RequestParam(value = "shops[]") Optional<String[]> shopIds,
                                                   Model model) throws Exception {
        FirebaseController.loadDbInfo(true);

        Merchant toRet = new Merchant(userId);

        if (shopIds.isPresent()) {
            String[] currShopIds = shopIds.get();
            Shop currShop = null;
            for (String currId : currShopIds) {
                try {
                    currShop = FirebaseController.getShopWithId(currId);
                } catch (Exception e) {
                    System.out.println(String.format("Error in signInAsMerchant: Could not find shop with id: %s", currId));
                    e.printStackTrace();
                }
                toRet.appendNewShop(currShop);
            }
        }

        toRet.setUserName(name);

        FirebaseController.setCurrUser(toRet);

        return toRet;
    }

    @PostMapping("/loginAsCustomer")
    public String signInAsCustomer(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password, Model model) {
        // TODO: FIX

        //Navigate to appropriate page, use "Home" for now
        return "HomePage";
    }
}
