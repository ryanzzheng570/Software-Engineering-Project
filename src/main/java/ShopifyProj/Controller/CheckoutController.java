package ShopifyProj.Controller;

import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;


@Controller
public class CheckoutController {
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam(value = "item") String[] items, @RequestParam(value = "store") String store, Model model) {
        String itemIds = "";
        for(int a = 0; a < items.length; a++) {
            itemIds += items[a];
            if (a != items.length -1) {
                itemIds += "$";
            }
        }

        model.addAttribute("itemIDs", itemIds);
        model.addAttribute("storeID", store);
        model.addAttribute("items", FirebaseController.getItemsFromStoreByIds(store, items));

        return "CheckoutPage";
    }

    @PostMapping("/checkout")
    public @ResponseBody Shop addToCart(@RequestParam(value = "storeId") String shopId,
                                        @RequestParam(value = "itemIds[]") String[] itemIds,
                                        @RequestParam(value = "quantities[]") int[] quantities,
                                        Model model) {
        Shop toMod = null;
        try {
            toMod = FirebaseController.getShopWithId(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> itemIdList = Arrays.asList(itemIds);

        for (Item currItem : toMod.getItems()) {
            String currId = currItem.getId();
            if (itemIdList.contains(currId)) {
                int ind = itemIdList.indexOf(currId);
                currItem.reduceQuantity(quantities[ind]);
            }
        }

        return toMod;
    }
}
