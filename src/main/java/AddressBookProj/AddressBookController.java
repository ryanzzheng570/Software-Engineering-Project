package AddressBookProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class AddressBookController {
    @Autowired
    private AddressBookRepository bookRepo;

    private final AtomicLong counter = new AtomicLong();

    @PostMapping("/addAddressBook")
    public @ResponseBody AddressBook addAddressBook() {
        AddressBook newBook = new AddressBook();
        //newBook.setId(Math.toIntExact(counter.incrementAndGet()));

        bookRepo.save(newBook);

        return newBook;
    }

}
