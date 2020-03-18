package ShopifyProj.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpController {
    @GetMapping("/signUp")
    public String viewSignUpPage(Model model) {
        return "SignUpPage";
    }
}
