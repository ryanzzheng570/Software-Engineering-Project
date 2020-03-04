import ShopifyProj.Images;
import ShopifyProj.Shop;
import ShopifyProj.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ShopTest {
    private Shop testShop;

    @Before
    public void setUp(){
        testShop = new Shop();
    }

    @After
    public void teardown(){
        testShop = null;
    }

    @Test
    public void testAddBuddy(){
        Item newItem = new Item("ITEM_1", new ArrayList<Images>(), "0.00");

        testShop.addItem(newItem);

        assertEquals(testShop.getItem(newItem.getId()), newItem);
        assertEquals(testShop.getItems().size(), 1);
    }

    @Test
    public void testRemoveBuddy(){
        Item newItem = new Item("ITEM_1", new ArrayList<Images>(), "0.00");

        testShop.addItem(newItem);
        testShop.removeItemWithId(newItem.getId());

        assertEquals(testShop.getItems().size(), 0);
    }

    @Test
    public void testClearBuddies(){
        Item buddy1 = new Item("ITEM_1", new ArrayList<Images>(), "0.00");
        Item buddy2 = new Item("ITEM_2", new ArrayList<Images>(), "0.00");
        Item buddy3 = new Item("ITEM_3", new ArrayList<Images>(), "0.00");

        testShop.addItem(buddy1);
        testShop.addItem(buddy2);
        testShop.addItem(buddy3);

        assertEquals(testShop.getItems().size(), 3);

        testShop.clearItems();

        assertEquals(testShop.getItems().size(), 0);
    }
}