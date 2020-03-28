package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Merchant;
import ShopifyProj.Model.Shop;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String viewHomePage(Model model) throws Exception {
        Customer temp = new Customer();
        temp.setId("-M3NF6_5RmDrIcO8cTQz");
        temp.setUserName("tempUserName");
        FirebaseController.setCurrUser(temp);
        FirebaseController.loadDbInfo(true);
        String username = "";
        boolean isCustomer = false;
        if(FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);
        return "HomePage";
    }
}
