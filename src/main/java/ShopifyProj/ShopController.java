package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ShopController {
    @Autowired
    private ShopRepository shopRepo;

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

    @PostMapping("/search")
    public @ResponseBody ArrayList<Shop> search(@RequestParam(value = "searchField") String query) {
        ArrayList<Shop> matchingShops = new ArrayList<>();
        for (Shop shop : shopRepo.findAll()) {
            boolean isAdded = false;

            Set<Tag> tags = shop.getTags();

            //Contains method does not work with tag name, need to iterate through
            for (Tag t : tags) {
                if(t.getTagName().equals(query) || t.getTagName().contains(query)) {
                    matchingShops.add(shop);
                    isAdded = true;
                }
            }

            if (shop.getShopName().equals(query) && isAdded == false) {
                matchingShops.add(shop);
            }
        }
        return matchingShops;
    }

}
