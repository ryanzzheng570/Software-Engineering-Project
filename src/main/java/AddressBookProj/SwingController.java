package AddressBookProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SwingController {
    @Autowired
    private AddressBookRepository bookRepo;

    @Autowired
    private BuddyInfoRepository buddyRepo;

    @Autowired
    private SwingDisplay view;

    @Autowired
    private AddressBook model;

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public SwingController(SwingDisplay view, AddressBook model) {
        this.view = view;
        this.model = model;

        view.setController(this);

        //SpringApplication.run(SwingController.class);

        //bookRepo.save(model);
    }

    public ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<String>();

        for (BuddyInfo buddy : model.getMyBuddies()) {
            data.add(buddy.toString());
        }

        return data;
    }

    public void handleButtonPress(String buttonDescriptor) {
        switch (buttonDescriptor) {
            case "CLEAR_BUDDIES":
                handleClear();
                break;
            case "REMOVE_BUDDY":
                handleRemoveBuddy();
                break;
            case "ADD_BUDDY":
                handleAddBuddy();
                break;
            case "READ_DB":
                handleReadDb();
                break;
            default:
                break;
        }
    }

    private void handleReadDb() {
        ArrayList<String> data  = new ArrayList<String>();
        for (BuddyInfo buddy : buddyRepo.findAll()){
            data.add(buddy.toString());
        }
        view.displayDataInAlert(data);
    }

    private void handleClear() {
        for (BuddyInfo buddy : model.getMyBuddies()) {
            buddyRepo.deleteById(buddy.getId());
        }
        model.clearBuddies();
    }

    private void handleRemoveBuddy() {
        int ind = view.getSelectedBuddy();
        if (ind >= 0) {
            int id = model.getBuddy(ind).getId();
            model.removeBuddy(ind);

            buddyRepo.deleteById(id);
        }
    }

    private void handleAddBuddy() {
        String name = view.getName();

        int phoneNum = -1;
        try {
            phoneNum = Integer.parseInt(view.getPhoneNumber());
        } catch (Exception e) {
            phoneNum = -1;
        }

        String addr = view.getAddress();
        BuddyInfo newBuddy = new BuddyInfo(name, phoneNum, addr);

        model.addBuddy(newBuddy);

        System.out.println(model.getId());
        Optional<AddressBook> dbCopy = bookRepo.findById(model.getId());
        if (dbCopy.isPresent()) {
            AddressBook tempBook = dbCopy.get();
            tempBook.addBuddy(newBuddy);
            bookRepo.save(tempBook);
        } else {
            bookRepo.save(model);
        }
    }

    /**
    public static void main(String args[]){
        AddressBook book = new AddressBook();
        SwingDisplay disp = new SwingDisplay();

        SwingController controller = new SwingController(disp, book);
        disp.setController(controller);
    }
     **/
}
