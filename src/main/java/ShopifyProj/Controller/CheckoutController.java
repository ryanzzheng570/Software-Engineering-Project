package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Model.TempItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;


@Controller
public class CheckoutController {
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam(value = "item") String[] items, @RequestParam(value = "store") String store, Model model) {
//        System.out.println("Store: " + store);

//        System.out.println("Items: " + items);

        String itemIds = "";
        for(int a= 0; a < items.length; a++) {
            itemIds += items[a];
            if (a != items.length -1) {
                itemIds += "$";
            }
        }
//        System.out.println("Items: " + itemIds);
        model.addAttribute("itemIDs", itemIds);
        model.addAttribute("storeID", store);
        model.addAttribute("items", FirebaseController.getItemsFromStoreByIds(store, items));
        return "CheckoutPage";
    }
}
