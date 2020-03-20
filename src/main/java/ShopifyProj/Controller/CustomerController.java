package ShopifyProj.Controller;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Item;
import ShopifyProj.Model.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class CustomerController {
    @GetMapping("/createCustomerAccount")
    public String viewCreateCustomerAccountPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "CreateCustomerAccountPage";
    }

    @PostMapping("/createCustomerAccount")
    public @ResponseBody Customer createCustomerAccount(@RequestParam(value = "userName") String userName,
                                                        @RequestParam(value = "password") String password,
                                                        @RequestParam(value = "email") String email,
                                                        @RequestParam(value = "address") String address,
                                                        @RequestParam(value = "phoneNumber") String phoneNum,
                                                        @RequestParam(value = "note") String note,
                                                        @RequestParam(value = "setId") String newId,
                                                        Model model) {
        Set<Item> boughtItems = new HashSet<Item>();
        Set<Item> cart = new HashSet<Item>();

        Customer toAdd = new Customer(userName, email, address, phoneNum, note, boughtItems, cart, password);
        toAdd.setId(newId);

        //TODO: FIX
        //customerRepository.save(customer);
        /*
         todo Should navigate to profile page, redirect to home page for now
         */
        return toAdd;
    }
}
