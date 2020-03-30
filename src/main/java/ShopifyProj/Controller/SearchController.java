package ShopifyProj.Controller;

import ShopifyProj.Model.Shop;
import ShopifyProj.Model.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.ArrayList;

@Controller
public class SearchController {
    private static final String PRODUCTION_MODE = "false";

    @GetMapping("/search")
    public String viewSearchPage(Model model) {
        String username = "";
        boolean isCustomer = false;
        if (FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);
        return "ShopSearchPage";
    }

    @PostMapping("/search")
    public @ResponseBody
    ArrayList<Shop> search(@RequestParam(value = "searchField") String query, @RequestParam(value = "testMode", defaultValue = "false") String testMode) {
        if (testMode.equals(PRODUCTION_MODE)) {
            FirebaseController.loadDbInfo(true);
        } else {
            FirebaseController.loadDbInfo(false);
        }

        ArrayList<Shop> matchingShops = new ArrayList<>();

        String lowercaseQuery = query.toLowerCase();

        for (Shop shop : FirebaseController.getDbShops()) {
            boolean isAdded = false;

            Set<Tag> tags = shop.getTags();
            for (Tag t : tags) {
                if (t.getTagName().equalsIgnoreCase(lowercaseQuery) || t.getTagName().toLowerCase().contains(lowercaseQuery) && isAdded == false) {
                    matchingShops.add(shop);
                    isAdded = true;
                }
            }

            if (shop.getShopName().equalsIgnoreCase(lowercaseQuery) || shop.getShopName().toLowerCase().contains(lowercaseQuery) && isAdded == false) {
                matchingShops.add(shop);
            }
        }
        return matchingShops;
    }

}
