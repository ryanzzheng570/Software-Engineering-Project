package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/createCustomerAccount")
    public @ResponseBody Customer createCustomerAccount(@ModelAttribute Customer newCustomer, Model model) {
        customerRepository.save(newCustomer);
        return newCustomer;
    }

    @GetMapping("/createCustomerAccount")
    public String viewCreateCustomerAccountPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "createCustomerAccount";
    }
}
