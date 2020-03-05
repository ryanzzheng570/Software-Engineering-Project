package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class ThymeController {
    @Autowired
    private ShopRepository shopRepo;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MerchantRepository merchantRepository;

    private List<Shop> getShops() {
        List<Shop> shops = new ArrayList<Shop>();
        for (Shop shop : shopRepo.findAll()) {
            shops.add(shop);
        }

        return shops;

    }

    private Shop getShopById(int aShopId) {
        return shopRepo.findById(aShopId);
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        return "homepage";
    }

    @GetMapping("/goToAddShopPage")
    public String viewAddShopPage(Model model) {
        model.addAttribute("shops", getShops());
        model.addAttribute("shop", new Shop());

        return "addShopPage";
    }


    @GetMapping("/goToShop")
    public String viewShopPageById(@RequestParam(value = "shopId") int aShopId, Model model) {
        model.addAttribute("shop", getShopById(aShopId));
        return "shopPage";
    }

    @GetMapping("/addNewMerchant")
    public String viewAddNewMerchantPage(Model model) {
        model.addAttribute("merchant", new Merchant());
        return "addMerchantPage";
    }

    @PostMapping("/addNewMerchant")
    public String addNewMerchant(@ModelAttribute Merchant newMerchant, Model model) {
        merchantRepository.save(newMerchant);
        return "/";
    }

    @GetMapping("/createCustomerAccount")
    public String viewCreateCustomerAccountPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "createCustomerAccount";
    }

    @PostMapping("/createCustomerAccount")
    public String createCustomerAccount(@ModelAttribute Customer newCustomer, Model model) {
        customerRepository.save(newCustomer);
        return "/";
    }

}
