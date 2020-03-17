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
        // You have an array of item IDs, and the store ID
        System.out.println("Store: " + store);

        System.out.println("Items: " + items);
        model.addAttribute("storeID", store);

        String itemIds = "";
        for(int a= 0; a < items.length; a++) {
            itemIds += items[a];
            if (a != items.length -1) {
                itemIds += "$";
            }
        }
        System.out.println("Items: " + itemIds);
        model.addAttribute("itemIDs", itemIds);
        model.addAttribute("items", FirebaseController.getItemsFromStoreByIds(store, items));
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
//        ArrayList<Integer> quantities = new ArrayList<Integer>();
//
//        for(int i= 0; i < items.length; i++) {
//            System.out.println("quantity "+ quantity[i]);
//            quantities.add(Integer.parseInt(quantity[i]));
//        }
//
//
//        System.out.println("Store: " + store);
//        System.out.println("paymentName " + paymentName);
//        System.out.println("ccNum " + ccNum);
//
//        boolean isSuccess = FirebaseController.purchaseItems(store, items, quantities);
//        System.out.println("Success: " + isSuccess);
//        return "CheckoutPage";
//    }

}
