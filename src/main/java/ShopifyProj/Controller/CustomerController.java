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
        String username = "";
        boolean isCustomer = false;
        if(FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);

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

        Customer toAdd = new Customer(userName, email, address, phoneNum, note, boughtItems, password);
        toAdd.setId(newId);

        FirebaseController.setCurrUser(toAdd);

        return toAdd;
    }
}
