package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Set;


@Controller
public class CheckoutController {
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam(value = "item") String[] items, @RequestParam(value = "store") String store, Model model) {
        // You have an array of item IDs, and the store ID
        System.out.println("Store: " + store);
        String itemIds = "";
        for(int a= 0; a < items.length; a++) {
            itemIds += items[a];
            if (a != items.length -1) {
                itemIds += "$";
            }
        }
        System.out.println("Items: " + itemIds);
        model.addAttribute("itemIds", itemIds);
        return "CheckoutPage";
    }

//    @PostMapping("/checkout")
//    public String checkout(@RequestParam(value = "item") String[] items,
//                           @RequestParam(value = "quantity") String[] quantity,
//                           @RequestParam(value = "store") String store,
//                           @RequestParam(value = "paymentName") String paymentName,
//                           @RequestParam(value = "ccNum") int ccNum,
//                           Model model) {
//
//        String itemIds = "";
//        String quantities = "";
//
//        for(int i= 0; i < items.length; i++) {
//            System.out.println("item "+ items[i]);
//            System.out.println("quantity "+ quantity[i]);
//            itemIds += items[i];
//            quantities += quantity[i];
//
//            if (i != items.length -1) {
//                itemIds += "$";
//                quantities += "$";
//            }
//        }
//
//
//        System.out.println("Store: " + store);
//        System.out.println("paymentName " + paymentName);
//        System.out.println("ccNum " + ccNum);
//
//        model.addAttribute("quantities", quantities);
//        model.addAttribute("itemIds", itemIds);
//        model.addAttribute("shopId", store);
//        return "CheckoutCompletePage";
//    }

}
