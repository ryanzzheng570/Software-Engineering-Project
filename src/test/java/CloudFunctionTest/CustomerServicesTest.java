package CloudFunctionTest;

import ShopifyProj.Controller.CloudTestController;
import ShopifyProj.Controller.FirebaseController;

import com.google.firebase.database.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


import java.util.HashMap;
import java.util.Map;

public class CustomerServicesTest {
    private static FirebaseDatabase testDbInstance = null;
    private static CloudTestController functionCaller = null;
    private static String result = "";
    private static final String PASS_FLAG = "PASS";

    @Test
    public void itAddsItemToShoppingCart() {
        final String CUSTOMER_ID = "aCustomerID";
        final String SHOP_ID = "itPurchaseItemsFromStoresID";
        final String ADD_ITEM_NAME = "addItemSCName";
        final String ADD_ITEM_QTY = "300";
        final String ADD_ITEM_COST = "9.99";
        final String ADD_ITEM_ID = "addItemSCID";
        String key = "";

        DatabaseReference customerRef = testDbInstance.getReference("test/users/customers/" + CUSTOMER_ID);
        Map<String, Object> customMap = new HashMap<>();
        customMap.put("address", "123 Map St");
        customMap.put("email", "a@a.com");
        customMap.put("note", "");
        customMap.put("password", "aPassword");
        customMap.put("phoneNum", "(xxx)xxx-xxxx");
        customMap.put("userName", "aUsername");
        customerRef.updateChildrenAsync(customMap);

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + ADD_ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", ADD_ITEM_NAME);
        map.put("cost", ADD_ITEM_COST);
        map.put("inventory", ADD_ITEM_QTY);
        firstItemRef.updateChildrenAsync(map);

        firebaseDelay();
        HashMap<String, String> param = new HashMap<>();
        param.put("customerID", CUSTOMER_ID);
        param.put("shopID", SHOP_ID);
        param.put("itemID", ADD_ITEM_ID);

        try {
            key = functionCaller.sendPost(CloudTestController.addItemToSC, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/users/customers/" + CUSTOMER_ID + "/shoppingCart/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String ret = (String)((Map<String, Object>)dataSnapshot.getValue()).get("itemID");
//                Map<String, Object> secondItemData = (Map<String, Object>) curStoreData.get(SECOND_ITEM_ID);
//                String secInvVal = (String) secondItemData.get("inventory");
//                assertThat("Incorrect inventory", secInvVal, is(SECOND_ITEM_QTY));
                setResult(PASS_FLAG);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        assertThat("The item was not added to the shopping cart correctly.", getResult(), is(PASS_FLAG));
    }

    @Test
    public void itPreventsPurchaseOfOutOfInventoryItems() {
        final String SHOP_ID = "itPurchaseItemsFromStoresID";
        final String FIRST_ITEM_ID = "firstItemID";
        final String FIRST_ITEM_NAME = "firstItem";
        final String FIRST_ITEM_QTY = "30";
        final String FIRST_ITEM_COST = "4.99";
        final String SECOND_ITEM_ID = "secondItemID";
        final String SECOND_ITEM_NAME = "secondItem";
        final String SECOND_ITEM_QTY = "1";
        final String SECOND_ITEM_COST = "1.99";
        final String SECOND_ITEM_PURC = "2";
        final String CUSTOMER_ID = "aCustomerID";

        DatabaseReference customerRef = testDbInstance.getReference("test/users/customers/" + CUSTOMER_ID);
        Map<String, Object> customMap = new HashMap<>();
        customMap.put("address", "123 Map St");
        customMap.put("email", "a@a.com");
        customMap.put("note", "");
        customMap.put("password", "aPassword");
        customMap.put("phoneNum", "(xxx)xxx-xxxx");
        customMap.put("userName", "aUsername");
        customerRef.updateChildrenAsync(customMap);

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + FIRST_ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", FIRST_ITEM_NAME);
        map.put("cost", FIRST_ITEM_COST);
        map.put("inventory", FIRST_ITEM_QTY);
        firstItemRef.updateChildrenAsync(map);

        DatabaseReference secItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + SECOND_ITEM_ID);
        Map<String, Object> secMap = new HashMap<>();
        secMap.put("name", SECOND_ITEM_NAME);
        secMap.put("cost", SECOND_ITEM_COST);
        secMap.put("inventory", SECOND_ITEM_QTY);
        secItemRef.updateChildrenAsync(secMap);

        firebaseDelay();
        HashMap<String, String> param = new HashMap<>();
        param.put("shopIDs", SHOP_ID);
        param.put("itemIDs", SECOND_ITEM_ID);
        param.put("quantities", SECOND_ITEM_PURC);
        param.put("customerID", CUSTOMER_ID);

        try {
            functionCaller.sendPost(CloudTestController.purchaseItemFromShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();
        testDbInstance.getReference("test/store/" + SHOP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> curStoreData = (Map<String, Object>) ((Map<String, Object>) dataSnapshot.getValue()).get("item");
                Map<String, Object> firstItemData = (Map<String, Object>) curStoreData.get(FIRST_ITEM_ID);
                String inv = (String) firstItemData.get("inventory");
                assertThat("Incorrect inventory", inv, is(FIRST_ITEM_QTY));

                Map<String, Object> secondItemData = (Map<String, Object>) curStoreData.get(SECOND_ITEM_ID);
                String secInvVal = (String) secondItemData.get("inventory");
                assertThat("Incorrect inventory", secInvVal, is(SECOND_ITEM_QTY));
                setResult(PASS_FLAG);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        // Need second delay because slow update of cloud function
        firebaseDelay();
        assertThat("Database did not update correctly after trying to purchase out of stock items.", getResult(), is(PASS_FLAG));
    }

    @Test
    public void itPurchasesItemsFromStores() {
        final String SHOP_ID = "itPurchaseItemsFromStoresID";
        final String FIRST_ITEM_ID = "firstItemID";
        final String FIRST_ITEM_NAME = "firstItem";
        final String FIRST_ITEM_QTY = "30";
        final String FIRST_ITEM_COST = "4.99";
        final String SECOND_ITEM_ID = "secondItemID";
        final String SECOND_ITEM_NAME = "secondItem";
        final String SECOND_ITEM_QTY = "5";
        final String SECOND_ITEM_COST = "1.99";
        final String FIRST_ITEM_PURC = "3";
        final String SECOND_ITEM_PURC = "1";
        final String CUSTOMER_ID = "aCustomerIDForThis";

        DatabaseReference customerRef = testDbInstance.getReference("test/users/customers/" + CUSTOMER_ID);
        Map<String, Object> customMap = new HashMap<>();
        customMap.put("address", "123 Map St");
        customMap.put("email", "a@a.com");
        customMap.put("note", "");
        customMap.put("password", "aPassword");
        customMap.put("phoneNum", "(xxx)xxx-xxxx");
        customMap.put("userName", "aUsername");
        customerRef.updateChildrenAsync(customMap);

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + FIRST_ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", FIRST_ITEM_NAME);
        map.put("cost", FIRST_ITEM_COST);
        map.put("inventory", FIRST_ITEM_QTY);
        firstItemRef.updateChildrenAsync(map);

        DatabaseReference secItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + SECOND_ITEM_ID);
        Map<String, Object> secMap = new HashMap<>();
        secMap.put("name", SECOND_ITEM_NAME);
        secMap.put("cost", SECOND_ITEM_COST);
        secMap.put("inventory", SECOND_ITEM_QTY);
        secItemRef.updateChildrenAsync(secMap);

        firebaseDelay();
        HashMap<String, String> param = new HashMap<>();
        param.put("shopIDs", SHOP_ID + ", " + SHOP_ID);
        param.put("itemIDs", FIRST_ITEM_ID + ", " + SECOND_ITEM_ID);
        param.put("quantities", FIRST_ITEM_PURC + ", " + SECOND_ITEM_PURC);
        param.put("customerID", CUSTOMER_ID);

        try {
            functionCaller.sendPost(CloudTestController.purchaseItemFromShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();
        testDbInstance.getReference("test/store/" + SHOP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> curStoreData = (Map<String, Object>) ((Map<String, Object>) dataSnapshot.getValue()).get("item");
                Map<String, Object> firstItemData = (Map<String, Object>) curStoreData.get(FIRST_ITEM_ID);
                Long invVal = (Long) firstItemData.get("inventory");
                int inventory = invVal != null ? invVal.intValue() : null;
                assertThat("Incorrect inventory", inventory, is(Integer.parseInt(FIRST_ITEM_QTY) - Integer.parseInt(FIRST_ITEM_PURC)));

                Map<String, Object> secondItemData = (Map<String, Object>) curStoreData.get(SECOND_ITEM_ID);
                Long secInvVal = (Long) secondItemData.get("inventory");
                int secInventory = secInvVal != null ? secInvVal.intValue() : null;
                assertThat("Incorrect inventory", secInventory, is(Integer.parseInt(SECOND_ITEM_QTY) - Integer.parseInt(SECOND_ITEM_PURC)));
                setResult(PASS_FLAG);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        // Need second delay because slow update of cloud function
        firebaseDelay();
        assertThat("Database did not update correctly after purchasing items.", getResult(), is(PASS_FLAG));
    }

    @Test
    public void itCreatesACustomer() {
        String key = "";
        final String USERNAME = "aUsername";
        final String NOTE = "thisIsANote";
        HashMap<String, String> param = new HashMap<>();
        param.put("userName", USERNAME);
        param.put("password", "aPassword");
        param.put("email", "a@a.com");
        param.put("address", "Carleton");
        param.put("phoneNumber", "1234567890");
        param.put("note", NOTE);

        try {
            key = functionCaller.sendPost(CloudTestController.createCustomer, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/users/customers/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> currItemData = (Map<String, Object>) dataSnapshot.getValue();

                String userName = (String) currItemData.get("userName");
                assertThat("Incorrect username", userName, is(USERNAME));
                String note = (String) currItemData.get("note");
                assertThat("Incorrect note", note, is(NOTE));
                setResult(PASS_FLAG);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        assertThat("Customer was not created correctly.", getResult(), is(PASS_FLAG));
    }


    public static void firebaseDelay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            fail("waitForCloudFunction exception\n" + ex.getStackTrace().toString());
        }
    }

    @BeforeClass
    public static void setup() {
        testDbInstance = FirebaseController.getInstance();
        functionCaller = new CloudTestController();
    }

    @AfterClass
    public static void cleanup() {
        testDbInstance.getReference("test/").removeValueAsync();
        firebaseDelay();
        testDbInstance = null;
        functionCaller = null;

    }

    @BeforeEach
    public static void resetResult() {
        result = "";
    }

    public static void setResult(String res) {
        result = res;
    }

    public static String getResult() {
        return result;
    }

}
