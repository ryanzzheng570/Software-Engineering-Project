package ShopifyProj;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

class CustomerTest {
    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        this.testCustomer = new Customer("TestName", "TestEmail", "Test Address",
                "123456789", "Testi note", new HashSet<Item>(), new HashSet<Item>() {
        });
    }

    @AfterEach
    public void tearDown() { testCustomer = null; }

    @Test
    public void appendNewBoughItem() {
        Item newItem = new Item("ITEM_1");
        testCustomer.appendNewBoughtItem(newItem);

        assertEquals(newItem, testCustomer.getBoughtItemById(newItem.getId()));
        assertEquals(1, testCustomer.getBoughtItems().size());
    }

    @Test
    public void appendNewCartItem() {
        Item newItem = new Item("ITEM_1");
        testCustomer.appendNewCartItem(newItem);

        assertEquals(newItem, testCustomer.getCartItemById(newItem.getId()));
        assertEquals(1, testCustomer.getCart().size());
    }

    @Test
    public void getBoughItemById() {
        Item newItem = new Item("ITEM_1");
        testCustomer.appendNewBoughtItem(newItem);
        assertEquals(newItem, testCustomer.getBoughtItemById(newItem.getId()));
    }

    @Test
    public void getCartItemById() {
        Item newItem = new Item("ITEM_1");
        testCustomer.appendNewCartItem(newItem);
        assertEquals(newItem, testCustomer.getCartItemById(newItem.getId()));
    }

    @Test
    public void removeBoughItemById() {
        Item newItem = new Item("ITEM_1");
        Item newItem2 = new Item("ITEM_2");

        testCustomer.appendNewBoughtItem(newItem);
        testCustomer.appendNewBoughtItem(newItem2);
        testCustomer.removeBoughtItemById(newItem.getId());

        assertEquals(newItem2, testCustomer.getBoughtItemById(newItem2.getId()));
        assertEquals(1, testCustomer.getBoughtItems().size());
    }

    @Test
    public void removeCartItemById() {
        Item newItem = new Item("ITEM_1");
        Item newItem2 = new Item("ITEM_2");

        testCustomer.appendNewCartItem(newItem);
        testCustomer.appendNewCartItem(newItem2);
        testCustomer.removeCartItemById(newItem.getId());

        assertEquals(newItem2, testCustomer.getCartItemById(newItem2.getId()));
        assertEquals(1, testCustomer.getCart().size());
    }
}