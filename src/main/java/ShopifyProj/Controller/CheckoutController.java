package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Model.TempItem;
import ShopifyProj.Repository.CustomerRepository;
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
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam(value = "item") String[] items, @RequestParam(value = "store") String store, Model model) {
        // Until database sorted out
        String aShopId2 = "-M2QECi8-MSD1yp8jzA9";

        // You have an array of item IDs, and the store ID
        System.out.println("Store: " + store);
//        String itemIds = "";
//        for(int a= 0; a < items.length; a++) {
//            itemIds += items[a];
//            if (a != items.length -1) {
//                itemIds += "$";
//            }
//        }
//        System.out.println("Items: " + itemIds);
//        model.addAttribute("itemIds", itemIds);
        System.out.println("Items: " + items);
        model.addAttribute("storeID", aShopId2);
//        model.addAttribute("items", FirebaseController.getItemsFromStoreByIds(aShopId2, items));
        TempItem[] testItems = FirebaseController.getItemsFromStoreByIds(aShopId2, items);
        model.addAttribute("items", new TempItem("test", 1, 2.99, "abc"));
        return "CheckoutPage";
    }


}
