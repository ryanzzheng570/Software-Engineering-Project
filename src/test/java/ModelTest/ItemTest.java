package ModelTest;

import ShopifyProj.Model.Image;
import ShopifyProj.Model.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ItemTest {
    private Item testItem;

    @Before
    public void setUp() { testItem = new Item(); }

    @After
    public void teardown() { testItem = null; }

    @Test
    public void testDecInventory(){
        int newInv = 10;

        testItem.setInventory(newInv);

        assertEquals(testItem.getInventory(), newInv);

        testItem.decrementInventory();

        assertEquals(testItem.getInventory(), newInv - 1);
    }

    @Test
    public void testReduceInventory(){
        int newInv = 10;
        int changeAmount = 5;

        testItem.setInventory(newInv);

        assertEquals(testItem.getInventory(), newInv);

        testItem.reduceQuantity(changeAmount);

        assertEquals(testItem.getInventory(), newInv - changeAmount);
    }
}
