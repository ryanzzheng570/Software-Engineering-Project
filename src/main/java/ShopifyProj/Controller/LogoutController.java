package ShopifyProj.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(Model model) {

        if (FirebaseController.getCurrUser() != null){
            FirebaseController.setCurrUser(null);
        }

        return "HomePage";
    }

}
