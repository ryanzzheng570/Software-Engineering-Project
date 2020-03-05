package ShopifyProj;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

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
        testCustomer.appendNewBoughItem(newItem);

        assertEquals(newItem, testCustomer.getBoughItemById(newItem.getId()));
        assertEquals(1, testCustomer.getBoughItems().size());
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
        testCustomer.appendNewBoughItem(newItem);
        assertEquals(newItem, testCustomer.getBoughItemById(newItem.getId()));
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

        testCustomer.appendNewBoughItem(newItem);
        testCustomer.appendNewBoughItem(newItem2);
        testCustomer.removeBoughItemById(newItem.getId());

        assertEquals(newItem2, testCustomer.getBoughItemById(newItem2.getId()));
        assertEquals(1, testCustomer.getBoughItems().size());
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