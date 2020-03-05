package ShopifyProj;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MerchantTest {
    private Merchant testMerchant;

    @BeforeEach
    void setUp() {
        testMerchant = new Merchant("TestMerchant", "1234596789", "testEmail@test.com");
    }

    @AfterEach
    void tearDown() {
        testMerchant = null;
    }

    @Test
    void appendNewShops() {
        Shop newShop = new Shop();

        testMerchant.appendNewShops(newShop);
        assertEquals(newShop, testMerchant.getShopById(newShop.getId()));
        assertEquals(1, testMerchant.getShops().size());
    }

    @Test
    void getShopById() {
        Shop newShop = new Shop();

        testMerchant.appendNewShops(newShop);
        assertEquals(newShop, testMerchant.getShopById(newShop.getId()));
    }

    @Test
    void removeShopById() {
        Shop newShop = new Shop();
        Shop newShop2 = new Shop();
        testMerchant.appendNewShops(newShop);
        testMerchant.appendNewShops(newShop2);
        testMerchant.removeShopById(newShop.getId());
        assertEquals(newShop2, testMerchant.getShopById(newShop2.getId()));
        assertEquals(1, testMerchant.getShops().size());
    }
}