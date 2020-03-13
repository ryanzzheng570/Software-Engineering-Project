package ShopifyProj.Controller;

import ShopifyProj.Model.Shop;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CheckoutController {

    @PostMapping("/addToCart")
    public String viewAddShopPage(Model model) {

        return "CheckoutPage";
    }
}
