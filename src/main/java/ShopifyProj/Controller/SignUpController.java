package ShopifyProj.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpController {
    @GetMapping("/signUp")
    public String viewSignUpPage(Model model) {
        String username = "";
        boolean isCustomer = false;
        if(FirebaseController.getCurrUser() != null) {
            username = FirebaseController.getCurrUser().getUserName();
            isCustomer = FirebaseController.isCurrUserCustomer();
        }
        model.addAttribute("username", username);
        model.addAttribute("isCustomer", isCustomer);
        return "SignUpPage";
    }
}
