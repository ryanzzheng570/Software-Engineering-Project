package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MerchantWebController {
    @Autowired
    private MerchantRepository merchantRepository;

    @PostMapping("/addNewMerchant")
    public @ResponseBody
    Merchant addNewMerchant(@ModelAttribute Merchant newMerchant, Model model) {
        merchantRepository.save(newMerchant);
        return newMerchant;
    }
}
