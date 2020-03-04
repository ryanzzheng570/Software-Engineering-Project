import ShopifyProj.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class ItemTest {
    private Item testItem;

    @Before
    public void setUp() { testItem = new Item(); }

    @After
    public void teardown() { testItem = null; }

    @Test
    public void testSetCost(){
        testItem.setCost("34.526");
        assertEquals(testItem.getCost(), "$34.53");
    }
}
