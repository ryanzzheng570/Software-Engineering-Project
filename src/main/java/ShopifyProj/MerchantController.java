package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MerchantController {
    @Autowired
    private MerchantRepository merchantRepository;

    @GetMapping("/addNewMerchant")
    public String viewAddNewMerchantPage(Model model) {
        model.addAttribute("merchant", new Merchant());
        return "addMerchantPage";
    }

    @PostMapping("/addNewMerchant")
    public @ResponseBody
    Merchant addNewMerchant(@ModelAttribute Merchant newMerchant, Model model) {
        merchantRepository.save(newMerchant);
        return newMerchant;
    }
}
