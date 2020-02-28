import AddressBookProj.BuddyInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;

import java.util.List;

import static org.junit.Assert.*;

public class BuddyInfoTest {
    private final String ORIG_NAME = "TEST_NAME";
    private final int ORIG_NUM = 0;
    private final String ORIG_ADDR = "TEST_ADDRESS";

    private BuddyInfo testBuddy;

    @Before
    public void setUp(){
        testBuddy = new BuddyInfo(ORIG_NAME, ORIG_NUM, ORIG_ADDR);
    }

    @After
    public void teardown(){
        testBuddy = null;
    }

    @Test
    public void testName(){
        String testName = "NEW NAME";

        assertEquals(testBuddy.getName(), ORIG_NAME);

        testBuddy.setName(testName);
        assertEquals(testBuddy.getName(), testName);
    }

    @Test
    public void testNum(){
        int testNum = 2;

        assertEquals(testBuddy.getPhoneNumber(), ORIG_NUM);

        testBuddy.setPhoneNumber(testNum);
        assertEquals(testBuddy.getPhoneNumber(), testNum);
    }

    @Test
    public void testAddr(){
        String testAddr = "MAIN STREET";

        assertEquals(testBuddy.getAddress(), ORIG_ADDR);

        testBuddy.setAddress(testAddr);
        assertEquals(testBuddy.getAddress(), testAddr);
    }
}