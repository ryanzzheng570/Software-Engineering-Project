package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
public class CheckoutController {


    @GetMapping("/goToCart")
    public String goToCart(
            Model model) {
        FirebaseController.loadDbInfo(true);
        if (FirebaseController.getCurrUser() == null || (FirebaseController.getCurrUser() != null && !(FirebaseController.getCurrUser() instanceof Customer))) {
            return "CustomerLoginPage";
        }

        ArrayList<Item> retItems = new ArrayList<Item>();
        Object[] temp= FirebaseController.getShoppingCartItems(FirebaseController.getCurrUser().getId(), FirebaseController.PRODUCTION_MODE);
        ArrayList<String> storeIds = (ArrayList<String>)temp[0];
        ArrayList<String> itemIds = (ArrayList<String>)temp[1];
        for(int a = 0; a < storeIds.size(); a++) {
            Item tempItem = FirebaseController.getItemFromStore(storeIds.get(a), itemIds.get(a));
            if(tempItem != null) {
                retItems.add(tempItem);
            }
        }
        model.addAttribute("items", retItems);
        model.addAttribute("itemIDs", itemIds);
        model.addAttribute("storeIDs", storeIds);
        model.addAttribute("customer", FirebaseController.getCurrUser().getId());

        return "CheckoutPage";
    }
}
