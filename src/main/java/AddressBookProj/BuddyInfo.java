package AddressBookProj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Entity
public class BuddyInfo {
    private Integer id = null;

    private String name;
    private int phoneNumber;
    private String address;

    private static final AtomicLong buddyCounter = new AtomicLong();

    @Autowired
    public BuddyInfo() {
        setName("");
        setPhoneNumber(-1);
        setAddress("");
        setId(Math.toIntExact(buddyCounter.incrementAndGet()));
    }

    public BuddyInfo(String name, int phoneNumber, String address) {
        setName(name);
        setPhoneNumber(phoneNumber);
        setAddress(address);
        setId(Math.toIntExact(buddyCounter.incrementAndGet()));
    }

    @Id
    //@GeneratedValue
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer newId) {
        this.id = newId;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getName(){
        return(this.name);
    }

    public void setPhoneNumber(int newNum)
    {
        this.phoneNumber = newNum;
    }

    public int getPhoneNumber(){

        return(this.phoneNumber);
    }

    public void setAddress(String newAddr) {
        this.address = newAddr;
    }

    public String getAddress() {

        return(this.address);
    }

    @Override
    public String toString() {
        String toRet = String.format("Buddy: Name: %s, Id: %d, Phone Number: %d, Address: %s",
                                     this.getName(),
                                     this.getId(),
                                     this.getPhoneNumber(),
                                     this.getAddress());

        return(toRet);
    }
}
