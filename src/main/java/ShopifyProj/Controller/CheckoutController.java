package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Item;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;

@Controller
public class CheckoutController {
    private static final String PRODUCTION_MODE = "false";

    @GetMapping("/goToCart")
    public String goToCart(@RequestParam(value = "testMode", defaultValue = "false") String testMode,
                           Model model) {
        if (testMode.equals(PRODUCTION_MODE)) {
            FirebaseController.loadDbInfo(true);
        } else {
            FirebaseController.loadDbInfo(false);
        }
        if (FirebaseController.getCurrUser() == null || (FirebaseController.getCurrUser() != null && !(FirebaseController.getCurrUser() instanceof Customer))) {
            String username = "";
            boolean isCustomer = false;
            if (FirebaseController.getCurrUser() != null) {
                username = FirebaseController.getCurrUser().getUserName();
                isCustomer = FirebaseController.isCurrUserCustomer();
            }
            model.addAttribute("username", username);
            model.addAttribute("isCustomer", isCustomer);
            return "CustomerLoginPage";
        }

        ArrayList<Item> retItems = new ArrayList<Item>();
        Object[] temp = FirebaseController.getShoppingCartItems(FirebaseController.getCurrUser().getId(), FirebaseController.PRODUCTION_MODE);
        ArrayList<String> storeIds = (ArrayList<String>) temp[0];
        ArrayList<String> itemIds = (ArrayList<String>) temp[1];
        for (int a = 0; a < storeIds.size(); a++) {
            Item tempItem = FirebaseController.getItemFromStore(storeIds.get(a), itemIds.get(a));
            if (tempItem != null) {
                retItems.add(tempItem);
            }
        }
        model.addAttribute("items", retItems);
        model.addAttribute("itemIDs", itemIds);
        model.addAttribute("storeIDs", storeIds);
        model.addAttribute("customer", FirebaseController.getCurrUser());
        model.addAttribute("isCustomer", FirebaseController.isCurrUserCustomer());

        return "CheckoutPage";
    }
}
