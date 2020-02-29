package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ShopController {
    @Autowired
    private ShopRepository shopRepo;

    @PostMapping("/addShop")
    public @ResponseBody Shop addShop() {
        Shop newShop = new Shop();

        shopRepo.save(newShop);

        return newShop;
    }

    @PostMapping("/searchForShops")
    public @ResponseBody ArrayList<Shop> search() {
        ArrayList<Shop> matchingShops = new ArrayList<>();
        for (Shop shop : shopRepo.findAll()) {
//            if (shop.getTags().contains(query) || shop.getShopName().equals(query)) {
                matchingShops.add(shop);
//            }
        }
        return matchingShops;
    }

}
