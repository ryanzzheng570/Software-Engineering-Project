package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerWebController {
    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/createCustomerAccount")
    public @ResponseBody Customer createCustomerAccount(@ModelAttribute Customer newCustomer, Model model) {
        customerRepository.save(newCustomer);
        return newCustomer;
    }
}
