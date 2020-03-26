package ShopifyProj.Controller;

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
    public String goToCart(@RequestParam(value = "userID") String user,
                           Model model) {
//        String itemIds = "";
//        ArrayList<Item> retItems = new ArrayList<Item>();
//
//        if (items.isPresent()) {
//            for (int a = 0; a < items.get().length; a++) {
//                itemIds += items.get()[a];
//                if (a != items.get().length - 1) {
//                    itemIds += "$";
//                }
//            }
//
//            retItems = FirebaseController.getItemsFromStoreByIds(store, items.get());
//        }
//
//        model.addAttribute("itemIDs", itemIds);
//        model.addAttribute("storeID", store);
//        model.addAttribute("items", retItems);

//        return "CheckoutPage";
        System.out.println("HERE");
        return "HomePage";
    }


    @PostMapping("/addToCart")
    public String addToCart(@RequestParam(value = "item") Optional<String[]> items,
                            @RequestParam(value = "store") String store,
                            Model model) {
        String itemIds = "";
        ArrayList<Item> retItems = new ArrayList<Item>();

        if (items.isPresent()) {
            for (int a = 0; a < items.get().length; a++) {
                itemIds += items.get()[a];
                if (a != items.get().length - 1) {
                    itemIds += "$";
                }
            }

            retItems = FirebaseController.getItemsFromStoreByIds(store, items.get());
        }

        model.addAttribute("itemIDs", itemIds);
        model.addAttribute("storeID", store);
        model.addAttribute("items", retItems);

        return "CheckoutPage";
    }

    @PostMapping("/checkout")
    public @ResponseBody
    Shop checkoutUser(@RequestParam(value = "storeId") String shopId,
                      @RequestParam(value = "itemIds[]") Optional<String[]> itemIds,
                      @RequestParam(value = "quantities[]") Optional<int[]> quantities,
                      Model model) {
        Shop toMod = null;
        try {
            toMod = FirebaseController.getShopWithId(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (itemIds.isPresent() && (quantities.isPresent())) {
            List<String> itemIdList = Arrays.asList(itemIds.get());

            for (Item currItem : toMod.getItems()) {
                String currId = currItem.getId();
                if (itemIdList.contains(currId)) {
                    int ind = itemIdList.indexOf(currId);
                    currItem.reduceQuantity(quantities.get()[ind]);
                }
            }
        }

        return toMod;
    }
}
