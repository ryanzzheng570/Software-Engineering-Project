package AddressBookProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList; // import the ArrayList class
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class AddressBook {
    private int id;

    private List<BuddyInfo> myBuddies;

    private static final AtomicLong counter = new AtomicLong();

    @Autowired
    public AddressBook()
    {
        clearBuddies();
        this.id = Math.toIntExact(counter.incrementAndGet());
    }

    @Id
    //@GeneratedValue
    public int getId() {
        return this.id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public void addBuddy(BuddyInfo newBuddy) {
        myBuddies.add(newBuddy);
    }

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public List<BuddyInfo> getMyBuddies(){
        return(myBuddies);
    }

    public void setMyBuddies(List<BuddyInfo> newBuddyList) {
        this.myBuddies = newBuddyList;
    }

    public void clearBuddies(){
        myBuddies = new ArrayList<BuddyInfo>();
    }

    public BuddyInfo getBuddy(int ind) {
        BuddyInfo toRet = null;

        if (ind < myBuddies.size()) {
            toRet = myBuddies.get(ind);
        }

        return(toRet);
    }

    public void removeBuddy(int ind){
        System.out.println("HERE");
        System.out.println(ind);
        if (ind < myBuddies.size()) {
            myBuddies.remove(ind);
        }
    }

    public void removeBuddyWithId(int id) {
        int ind = 0;
        while (ind < myBuddies.size()){
            if (myBuddies.get(ind).getId().equals(id)) {
                break;
            }
            ind += 1;
        }
        this.removeBuddy(ind);
    }

    @Override
    public String toString() {
        String toRet = "";
        toRet += String.format("Address Book (Id: %d): \n", this.getId());

        for (BuddyInfo buddy : myBuddies) {
            toRet += "\t" + buddy.toString() + "\n";

        }

        return(toRet);
    }

    /**
    public static void main(String args[]) {
        AddressBook myBook = new AddressBook();

        BuddyInfo buddy1 = new BuddyInfo("Dan", 1, "1 Main Street");
        BuddyInfo buddy2 = new BuddyInfo("Paul", 2, "2 Main Street");
        BuddyInfo buddy3 = new BuddyInfo("George", 3, "3 Main Street");

        myBook.addBuddy(buddy1);
        myBook.addBuddy(buddy2);
        myBook.addBuddy(buddy3);

        System.out.println(myBook);
    }
     **/
}
