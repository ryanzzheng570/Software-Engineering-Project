package AddressBookProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Null;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class BuddyInfoController {
    @Autowired
    private AddressBookRepository bookRepo;

    @Autowired
    private BuddyInfoRepository buddyRepo;

    private final AtomicLong controllerCounter = new AtomicLong();

    @PostMapping("/addBuddy")
    public BuddyInfo addBuddy(@RequestParam(value = "name") String name,
                              @RequestParam(value = "phoneNumber") int phoneNumber,
                              @RequestParam(value = "address") String address,
                              @RequestParam(value = "bookId") int bookId) {
        BuddyInfo newBuddy = null;

        if (bookId == -1) {
            for (AddressBook book : bookRepo.findAll()) {
                bookId = book.getId();
            }
        }

        Optional<AddressBook> checkBook = bookRepo.findById(bookId);
        if (checkBook.isPresent()){
            AddressBook book = checkBook.get();
            System.out.println(book.toString());

            newBuddy = new BuddyInfo(name, phoneNumber, address);
            int newId = Math.toIntExact(controllerCounter.incrementAndGet());
            System.out.println(newId);
            //newBuddy.setId(newId);

            book.addBuddy(newBuddy);

            bookRepo.save(book);
        } else {
            System.out.println(String.format("No AddressBook with id: %d", bookId));

            for (AddressBook tempBook : bookRepo.findAll()){
                System.out.println(tempBook.toString());
            }
        }

        return newBuddy;
    }

    @PostMapping("/removeBuddy")
    public int removeBuddy(@RequestParam(value = "buddyId") int buddyId,
                                 @RequestParam(value = "bookId") int bookId) {
        Boolean retVal = false;

        BuddyInfo newBuddy = null;

        if (bookId == -1) {
            for (AddressBook book : bookRepo.findAll()) {
                bookId = book.getId();
            }
        }

        if (buddyId == -1) {
            for (BuddyInfo buddy : buddyRepo.findAll()) {
                buddyId = buddy.getId();
            }
        }

        Optional<AddressBook> checkBook = bookRepo.findById(bookId);
        if (checkBook.isPresent()){
            AddressBook book = checkBook.get();

            book.removeBuddyWithId(buddyId);

            bookRepo.save(book);
            buddyRepo.deleteById(buddyId);

            retVal = true;
        } else {
            System.out.println(String.format("No AddressBook with id: %d", bookId));

            for (AddressBook tempBook : bookRepo.findAll()){
                System.out.println(tempBook.toString());
            }
        }

        return buddyId;
    }
}
