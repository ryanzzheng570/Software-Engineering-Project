package ModelTest;

import ShopifyProj.Model.Customer;
import ShopifyProj.Model.Image;
import ShopifyProj.Model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

class CustomerTest {
    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        this.testCustomer = new Customer("TestName", "TestEmail", "Test Address",
                "123456789", "Test note", new HashSet<Item>(), new HashSet<Item>() {
        }, "Testing Password");
    }

    @AfterEach
    public void tearDown() { testCustomer = null; }

    @Test
    public void appendNewBoughItem() {
        Item newItem = new Item("ITEM_1", new ArrayList<Image>(), 0.0, 0);
        testCustomer.appendNewBoughtItem(newItem);

        assertEquals(newItem, testCustomer.getBoughtItemById(newItem.getId()));
        assertEquals(1, testCustomer.getBoughtItems().size());
    }

    @Test
    public void appendNewCartItem() {
        Item newItem = new Item("ITEM_1", new ArrayList<Image>(), 0.0, 0);
        testCustomer.appendNewCartItem(newItem);

        assertEquals(newItem, testCustomer.getCartItemById(newItem.getId()));
        assertEquals(1, testCustomer.getCart().size());
    }

    @Test
    public void getBoughItemById() {
        Item newItem = new Item("ITEM_1", new ArrayList<Image>(), 0.0, 0);
        testCustomer.appendNewBoughtItem(newItem);
        assertEquals(newItem, testCustomer.getBoughtItemById(newItem.getId()));
    }

    @Test
    public void getCartItemById() {
        Item newItem = new Item("ITEM_1", new ArrayList<Image>(), 0.0, 0);
        testCustomer.appendNewCartItem(newItem);
        assertEquals(newItem, testCustomer.getCartItemById(newItem.getId()));
    }

    @Test
    public void removeBoughtItemById() {
        Item newItem = new Item("ITEM_1", new ArrayList<Image>(), 0.0, 0);
        Item newItem2 = new Item("ITEM_2", new ArrayList<Image>(), 0.0, 0);

        testCustomer.appendNewBoughtItem(newItem);
        testCustomer.appendNewBoughtItem(newItem2);

        assertEquals(newItem, testCustomer.getBoughtItemById(newItem.getId()));
        assertEquals(newItem2, testCustomer.getBoughtItemById(newItem2.getId()));
        assertEquals(2, testCustomer.getBoughtItems().size());

        String idToRem = newItem.getId();

        testCustomer.removeBoughtItemById(idToRem);

        assertEquals(null, testCustomer.getBoughtItemById(idToRem));
        assertEquals(newItem2, testCustomer.getBoughtItemById(newItem2.getId()));
        assertEquals(1, testCustomer.getBoughtItems().size());
    }

    @Test
    public void removeCartItemById() {
        Item newItem = new Item("ITEM_1", new ArrayList<Image>(), 0.0, 0);
        Item newItem2 = new Item("ITEM_2", new ArrayList<Image>(), 0.0, 0);

        testCustomer.appendNewCartItem(newItem);
        testCustomer.appendNewCartItem(newItem2);

        assertEquals(newItem, testCustomer.getCartItemById(newItem.getId()));
        assertEquals(newItem2, testCustomer.getCartItemById(newItem2.getId()));
        assertEquals(2, testCustomer.getCart().size());

        String idToRem = newItem.getId();

        testCustomer.removeCartItemById(idToRem);

        assertEquals(null, testCustomer.getCartItemById(idToRem));
        assertEquals(newItem2, testCustomer.getCartItemById(newItem2.getId()));
        assertEquals(1, testCustomer.getCart().size());
    }
}