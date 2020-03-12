package ShopifyProj.Controller;

import ShopifyProj.Model.Shop;
import ShopifyProj.Repository.ShopRepository;
import ShopifyProj.Model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.ArrayList;

@Controller
public class SearchController {
    @Autowired
    private ShopRepository shopRepo;

    @GetMapping("/search")
    public String viewSearchPage(Model model) {
        return "ShopSearchPage";
    }

    @PostMapping("/search")
    public @ResponseBody ArrayList<Shop> search(@RequestParam(value = "searchField") String query) {
        ArrayList<Shop> matchingShops = new ArrayList<>();

        String lowercaseQuery = query.toLowerCase();

        for (Shop shop : shopRepo.findAll()) {
            boolean isAdded = false;

            Set<Tag> tags = shop.getTags();
            for (Tag t : tags) {
                if(t.getTagName().equalsIgnoreCase(lowercaseQuery) || t.getTagName().toLowerCase().contains(lowercaseQuery)) {
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
