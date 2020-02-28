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
    private AddressBookRepository bookRepo;

    private List<Integer> getIds() {
        List<Integer> ids = new ArrayList<Integer>();
        for (AddressBook book : bookRepo.findAll()) {
            Integer id = book.getId();
            ids.add(id);
        }

        return ids;

    }

    @GetMapping("viewAddressBookJson")
    public @ResponseBody AddressBook viewAddressBookJson(@RequestParam(value = "id") int bookId,
                               Model model) {
        if (bookId == -1) {
            for (AddressBook book : bookRepo.findAll()) {
                bookId = book.getId();
            }
        }

        AddressBook toView = null;

        Optional<AddressBook> checkBook = bookRepo.findById(bookId);
        if (checkBook.isPresent()){
            toView = checkBook.get();
        } else {
            System.out.println(String.format("No AddressBook with id: %d", bookId));
        }

        return toView;
    }

    @GetMapping("viewAddressBook")
    public String viewAddressBook(@RequestParam(value = "id") int bookId,
                                  Model model) {
        if (bookId == -1) {
            for (AddressBook book : bookRepo.findAll()) {
                bookId = book.getId();
            }
        }

        AddressBook toView = null;

        Optional<AddressBook> checkBook = bookRepo.findById(bookId);
        if (checkBook.isPresent()){
            toView = checkBook.get();
        } else {
            System.out.println(String.format("No AddressBook with id: %d", bookId));
        }

        model.addAttribute("book", toView);
        model.addAttribute("buddyInfo", new BuddyInfo());
        model.addAttribute("bookIds", getIds());
        model.addAttribute("addressBook", new AddressBook());

        return "AddressBook";
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("bookToView", null);
        model.addAttribute("buddyInfo", new BuddyInfo());
        model.addAttribute("bookIds", getIds());
        model.addAttribute("addressBook", new AddressBook());

        return "index";
    }


}
