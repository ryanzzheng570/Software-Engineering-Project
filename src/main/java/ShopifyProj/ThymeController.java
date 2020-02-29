package ShopifyProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ThymeController {
    @Autowired
    private ShopRepository shopRepo;

    private List<Integer> getIds() {
        List<Integer> ids = new ArrayList<Integer>();
        for (Shop shop : shopRepo.findAll()) {
            Integer id = shop.getId();
            ids.add(id);
        }

        return ids;

    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("shopIds", getIds());

        return "index";
    }

    @GetMapping("/search")
    public String viewSearchPage(Model model) {
        return "search";
    }

}
