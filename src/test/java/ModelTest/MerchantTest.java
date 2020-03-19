package ModelTest;

import ShopifyProj.Model.Merchant;
import ShopifyProj.Model.Shop;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MerchantTest {
    private Merchant testMerchant;

    @BeforeEach
    void setUp() {
        testMerchant = new Merchant("TestMerchant", "1234596789", "testEmail@test.com", "testingPassword");
    }

    @AfterEach
    void tearDown() {
        testMerchant = null;
    }

    @Test
    void testAppendNewShop() {
        Shop newShop = new Shop();

        testMerchant.appendNewShop(newShop);

        assertEquals(newShop, testMerchant.getShopById(newShop.getId()));
        assertEquals(1, testMerchant.getShops().size());
    }

    @Test
    void testGetShopById() {
        Shop newShop = new Shop();

        testMerchant.appendNewShop(newShop);

        assertEquals(newShop, testMerchant.getShopById(newShop.getId()));
        assertEquals(1, testMerchant.getShops().size());
    }

    @Test
    void testRemoveShopById() {
        Shop newShop = new Shop();
        Shop newShop2 = new Shop();

        testMerchant.appendNewShop(newShop);
        testMerchant.appendNewShop(newShop2);

        assertEquals(newShop, testMerchant.getShopById(newShop.getId()));
        assertEquals(newShop2, testMerchant.getShopById(newShop2.getId()));
        assertEquals(2, testMerchant.getShops().size());

        String idToRem = newShop.getId();

        testMerchant.removeShopById(idToRem);

        assertEquals(null, testMerchant.getShopById(idToRem));
        assertEquals(newShop2, testMerchant.getShopById(newShop2.getId()));
        assertEquals(1, testMerchant.getShops().size());
    }
}