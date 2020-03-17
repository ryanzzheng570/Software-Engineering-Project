package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Merchant;
import ShopifyProj.Model.User;
import ShopifyProj.Repository.CustomerRepository;
import ShopifyProj.Repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/login")
    public String viewLoginPage(Model model) {
        return "Login";
    }

    @GetMapping("/loginAsMerchant")
    public String viewMerchantLogin(Model model) {
        model.addAttribute("user", new Merchant());
        model.addAttribute("isLoginFailed", false);
        return "MerchantLoginPage";
    }

    @GetMapping("/loginAsCustomer")
    public String viewCustomerLogin(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("isLoginFailed", false);
        return "CustomerLoginPage";
    }

    @PostMapping("/loginAsMerchant")
    public String signInAsMerchant(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password, Model model) {
        Iterable<Merchant> merchants = merchantRepository.findAll();

        //Navigate to appropriate page, use "Home" for now
        return "HomePage";
    }

    @PostMapping("/loginAsCustomer")
    public String signInAsCustomer(@RequestParam(value = "userName") String userName, @RequestParam(value = "password") String password, Model model) {
        Iterable<Customer> customers = customerRepository.findAll();

        //Navigate to appropriate page, use "Home" for now
        return "HomePage";
    }
}
