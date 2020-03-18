package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CustomerController {
    @GetMapping("/createCustomerAccount")
    public String viewCreateCustomerAccountPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "CreateCustomerAccountPage";
    }

    @PostMapping("/createCustomerAccount")
    public String createCustomerAccount(@ModelAttribute Customer customer, Model model) {
        //TODO: FIX
        //customerRepository.save(customer);
        /*
         todo Should navigate to profile page, redirect to home page for now
         */
        return "redirect:/";
    }
}
