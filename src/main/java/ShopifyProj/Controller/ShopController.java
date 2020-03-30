package ShopifyProj.Controller;

import ShopifyProj.Model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
public class ShopController {
    @Autowired
    private MerchantController merchCont;
    private static final String PRODUCTION_MODE = "false";

    @GetMapping("/goToShopCustomerView")
    public String viewShopPageById(@RequestParam(value = "shopId") String aShopId, @RequestParam(value = "testMode", defaultValue = "false") String testMode,
                                   Model model) {
        if (testMode.equals(PRODUCTION_MODE)) {
            FirebaseController.loadDbInfo(true);
        } else {
            FirebaseController.loadDbInfo(false);
        }
        boolean isLoggedIn = false;
        String customerID = "";
        if (FirebaseController.getCurrUser() != null && FirebaseController.getCurrUser() instanceof Customer) {
            isLoggedIn = true;
            customerID = FirebaseController.getCurrUser().getId();
        }
        Shop shopToView = null;
        try {
            shopToView = FirebaseController.getShopWithId(aShopId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("shopID", aShopId);
        model.addAttribute("shop", shopToView);
        model.addAttribute("loggedIn", isLoggedIn);
        model.addAttribute("customerID", customerID);

        String username = "";
        boolean isCustomer = false;
        if (FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);

        return "CustomerShopViewPage";
    }

    @PostMapping("/updateShopId")
    public @ResponseBody
    Shop changeShopId(@RequestParam(value = "oldId") String oldId,
                      @RequestParam(value = "newId") String newId,
                      Model model) {
        Shop checkShop = null;
        try {
            checkShop = FirebaseController.getShopWithId(oldId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkShop.setId(newId);

        return checkShop;
    }

    @PostMapping("/changeShopName")
    public @ResponseBody
    Shop changeShopName(@RequestParam(value = "shopId") String shopId,
                        @RequestParam(value = "shopName") String newName,
                        Model model) {
        Shop checkShop = null;
        try {
            checkShop = FirebaseController.getShopWithId(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkShop.setShopName(newName);

        return checkShop;
    }

    @PostMapping("/removeTag")
    public @ResponseBody
    Shop removeTag(@RequestParam(value = "shopId") String shopId,
                   @RequestParam(value = "tagId") String tagId,
                   Model model) {
        Shop checkShop = null;
        try {
            checkShop = FirebaseController.getShopWithId(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkShop.removeTagWithId(tagId);

        return checkShop;
    }

    @PostMapping("/addTag")
    public @ResponseBody
    Tag addTag(@RequestParam(value = "shopId") String shopId,
               @RequestParam(value = "tagName") String tagName,
               @RequestParam(value = "setId") String tagId,
               Model model) {
        Shop checkShop = null;
        try {
            checkShop = FirebaseController.getShopWithId(shopId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Tag toAdd = new Tag(tagName);
        toAdd.setId(tagId);
        checkShop.addTag(toAdd);

        return toAdd;
    }

    @PostMapping("/addShop")
    public @ResponseBody
    Shop addShop(@RequestParam(value = "shopName") String name,
                 @RequestParam(value = "setId") String newId,
                 @RequestParam(value = "testMode", defaultValue = "false") String testMode,
                 Model model) {
        if (testMode.equals(PRODUCTION_MODE)) {
            FirebaseController.loadDbInfo(true);
        } else {
            FirebaseController.loadDbInfo(false);
        }
        Shop newShop = new Shop(name, Optional.empty());
        newShop.setId(newId);

        FirebaseController.addShop(newShop);

        ((Merchant) FirebaseController.getCurrUser()).appendNewShop(newShop);

        return newShop;
    }

    @GetMapping("/goToEditShopPage")
    public String displayYourShop(@RequestParam(value = "shopId") String shopId, Model model) {
        if (FirebaseController.getCurrUser() == null || (FirebaseController.getCurrUser() != null && FirebaseController.getCurrUser() instanceof Customer)) {
            return "MerchantLoginPage";
        } else {
            Shop checkShop = null;
            try {
                checkShop = FirebaseController.getShopWithId(shopId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            model.addAttribute("shop", checkShop);
            model.addAttribute("item", new Item());
            model.addAttribute("tag", new Tag());
            model.addAttribute("currUser", FirebaseController.getCurrUser());
            model.addAttribute("isCustomer", FirebaseController.isCurrUserCustomer());

            return "EditShopPage";
        }
    }
}
