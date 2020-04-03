package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Merchant;
import ShopifyProj.Model.Shop;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class LoginController {
    private static final String PRODUCTION_MODE = "false";

    @GetMapping("/login")
    public String viewLoginPage(Model model) {
        String username = "";
        boolean isCustomer = false;
        if (FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);

        return "Login";
    }

    @GetMapping("/loginAsMerchant")
    public String viewMerchantLogin(Model model) {
        String username = "";
        boolean isCustomer = false;
        if (FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);

        model.addAttribute("user", new Merchant());
        model.addAttribute("isLoginFailed", false);
        return "MerchantLoginPage";
    }

    @GetMapping("/loginAsCustomer")
    public String viewCustomerLogin(@RequestParam(value = "testMode", defaultValue = "false") String testMode,
                                    Model model) {
        if (testMode.equals(PRODUCTION_MODE)) {
            FirebaseController.loadDbInfo(true);
        } else {
            FirebaseController.loadDbInfo(false);
        }

        String username = "";
        boolean isCustomer = false;
        if (FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);


        model.addAttribute("customer", new Customer());
        model.addAttribute("isLoginFailed", false);
        return "CustomerLoginPage";
    }

    @PostMapping("/loginAsMerchant")
    public @ResponseBody
    Merchant signInAsMerchant(@RequestParam(value = "id") String userId,
                              @RequestParam(value = "userName") String name,
                              @RequestParam(value = "shops[]") Optional<String[]> shopIds,
                              @RequestParam(value = "testMode", defaultValue = "false") String testMode) throws Exception {
        if (testMode.equals(PRODUCTION_MODE)) {
            FirebaseController.loadDbInfo(true);
        } else {
            FirebaseController.loadDbInfo(false);
        }

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
    public @ResponseBody
    Customer signInAsCustomer(@RequestParam(value = "id") String userId,
                                    @RequestParam(value = "userName") String userName,
                                   @RequestParam(value = "cart[]") Optional<String[]> cartIds,
                                   @RequestParam(value = "testMode", defaultValue = "false") String testMode) {
        if(testMode.equals(PRODUCTION_MODE)) {
            FirebaseController.loadDbInfo(true);
        } else {
            FirebaseController.loadDbInfo(false);
        }

        Customer toRet = new Customer(userId);
        toRet.setUserName(userName);

        FirebaseController.setCurrUser(toRet);

        //Navigate to appropriate page, use "Home" for now
        return toRet;
    }
}
