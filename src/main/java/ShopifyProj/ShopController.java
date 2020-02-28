package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

}
