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
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ShopController {
    @Autowired
    private ShopRepository shopRepo;

    @PostMapping("/addShop")
    public @ResponseBody Shop addShop(@RequestParam(value = "shopName") String name,
                   @RequestParam(value = "tag") List<String> tags) {
        Set<Tag> tagSet = new HashSet<Tag>();

        for (String tag : tags) {
            tagSet.add(new Tag(tag));
        }

        Shop newShop = new Shop(name, Optional.of(tagSet));

        System.out.println(newShop.toString());

        shopRepo.save(newShop);

        return newShop;
    }

}
