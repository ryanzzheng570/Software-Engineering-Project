package ShopifyProj.Controller;

import ShopifyProj.Model.Item;
import ShopifyProj.Model.Shop;
import ShopifyProj.Repository.ShopRepository;
import ShopifyProj.Model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;

@Controller
public class ShopController {
    @Autowired
    private ShopRepository shopRepo;

    private Shop getShopById(int aShopId) {
        return shopRepo.findById(aShopId);
    }

    private List<Shop> getShops() {
        List<Shop> shops = new ArrayList<Shop>();
        for (Shop shop : shopRepo.findAll()) {
            shops.add(shop);
        }

        return shops;

    }

    @GetMapping("/goToAddShopPage")
    public String viewAddShopPage(Model model) {
        model.addAttribute("shops", getShops());
        model.addAttribute("shop", new Shop());

        return "CreateShopPage";
    }

    @GetMapping("/goToShopCustomerView")
    public String viewShopPageById(@RequestParam(value = "shopId") int aShopId, Model model) {
        model.addAttribute("shop", getShopById(aShopId));
        return "CustomerShopViewPage";
    }

    @PostMapping("/addShop")
    public @ResponseBody Shop addShop(@RequestParam(value = "shopName") String name,
                                      @RequestParam(value = "tag") Optional<List<String>> tags) {
        Set<Tag> tagSet = new HashSet<Tag>();

        if (tags.isPresent()) {
            for (String tag : tags.get()) {
                if (!tag.equals("")){
                    tagSet.add(new Tag(tag));
                }
            }
        }

        Shop newShop = new Shop(name, Optional.of(tagSet));

        shopRepo.save(newShop);

        return newShop;
    }

    @GetMapping("/goToShopMerchantView")
    public String displayYourShop(@RequestParam(value = "shopId") Integer shopId, Model model){

        Shop toView = null;

        Optional<Shop> theShop = shopRepo.findById(shopId);
        if(theShop.isPresent()){
            toView = theShop.get();
            System.out.println(toView);
        }else {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }

        model.addAttribute("shop", toView);
        model.addAttribute("item", new Item());

        return "MerchantShopViewPage";
    }
}
