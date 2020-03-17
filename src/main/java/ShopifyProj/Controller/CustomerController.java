package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {
    @PostMapping("/createCustomerAccount")
    public @ResponseBody Customer createCustomerAccount(@ModelAttribute Customer newCustomer, Model model) throws Exception {
        //TODO: FIX
        return newCustomer;
    }

    @GetMapping("/createCustomerAccount")
    public String viewCreateCustomerAccountPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "createCustomerAccount";
    }
}
