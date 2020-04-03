package CloudFunctionTest;

import ShopifyProj.Controller.CloudTestController;
import ShopifyProj.Controller.FirebaseController;

import com.google.firebase.database.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;


import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.HashMap;
import java.util.Map;

public class CustomerServicesTest {
    private static FirebaseDatabase testDbInstance = null;
    private static CloudTestController functionCaller = null;
    private String result = "";
    private final String PASS_FLAG = "PASS";
    private boolean DONE_FLAG = false;
    private int DELAY_COUNTER = 0;
    private final int MAX_DELAYS = 5;

    @Test
    public void itRemovesItemFromShoppingCart() {
        final String CUSTOMER_ID = "itRemovesItemFromShoppingCart";
        final String SHOP_ID = "aSHOPID";
        final String REMOVE_ITEM_NAME = "addItemSCName";
        final String REMOVE_ITEM_QTY = "300";
        final String REMOVE_ITEM_COST = "9.99";
        final String REMOVE_ITEM_ID = "secItemSCID";
        final String SECOND_ITEM_NAME = "secItemSCName";
        final String SECOND_ITEM_QTY = "30000";
        final String SECOND_ITEM_COST = "19.99";
        final String SECOND_ITEM_ID = "secItemSCID";

        DatabaseReference customerRef = testDbInstance.getReference("test/users/customers/" + CUSTOMER_ID);
        Map<String, Object> customMap = new HashMap<>();
        customMap.put("address", "123 Map St");
        customMap.put("email", "a@a.com");
        customMap.put("note", "");
        customMap.put("password", "aPassword");
        customMap.put("phoneNum", "(xxx)xxx-xxxx");
        customMap.put("userName", "aUsername");
        customerRef.updateChildrenAsync(customMap);

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + REMOVE_ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", REMOVE_ITEM_NAME);
        map.put("cost", REMOVE_ITEM_COST);
        map.put("inventory", REMOVE_ITEM_QTY);
        firstItemRef.updateChildrenAsync(map);

        DatabaseReference secItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + SECOND_ITEM_ID);
        Map<String, Object> secMap = new HashMap<>();
        secMap.put("name", SECOND_ITEM_NAME);
        secMap.put("cost", SECOND_ITEM_COST);
        secMap.put("inventory", SECOND_ITEM_QTY);
        secItemRef.updateChildrenAsync(secMap);

        firebaseDelay();
        HashMap<String, String> param = new HashMap<>();
        param.put("userID", CUSTOMER_ID);
        param.put("itemID", REMOVE_ITEM_ID);

        try {
            functionCaller.sendPost(CloudTestController.removeItemFromSC, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/users/customers/" + CUSTOMER_ID + "/shoppingCart/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot cartData : dataSnapshot.getChildren()) {
                    setResult("Shouldn't come here");
                    String itemID = (String) ((Map<String, Object>) cartData.getValue()).get("itemID");
                    String storeID = (String) ((Map<String, Object>) cartData.getValue()).get("shopID");
                    assertThat("Incorrect itemID", itemID, is(not(REMOVE_ITEM_ID)));
                }
                setResult(PASS_FLAG);
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult("FALSE");
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }
        assertThat("The item was not added to the shopping cart correctly.", getResult(), is(PASS_FLAG));
    }

    @Test
    public void itAddsItemToShoppingCart() {
        final String CUSTOMER_ID = "itAddsItemToShoppingCart";
        final String SHOP_ID = "aSHOPID";
        final String ADD_ITEM_NAME = "addItemSCName";
        final String ADD_ITEM_QTY = "300";
        final String ADD_ITEM_COST = "9.99";
        final String ADD_ITEM_ID = "addItemSCID";

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
            functionCaller.sendPost(CloudTestController.addItemToSC, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();
        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();
        testDbInstance.getReference("test/users/customers/" + CUSTOMER_ID + "/shoppingCart/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot cartData : dataSnapshot.getChildren()) {
                    String itemID = (String) ((Map<String, Object>) cartData.getValue()).get("itemID");
                    String storeID = (String) ((Map<String, Object>) cartData.getValue()).get("shopID");
                    toVerify.put("Incorrect item ID", new Object[]{itemID, ADD_ITEM_ID});
                    toVerify.put("Incorrect store ID.", new Object[]{storeID, SHOP_ID});
                    setResult(PASS_FLAG);
                }
                setDoneFlag(true);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult("FAIL");
                setDoneFlag(true);
            }

        });
        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }
        for (String reason : toVerify.keySet()) {
            Object val1 = toVerify.get(reason)[0];
            Object val2 = toVerify.get(reason)[1];

            System.out.println(String.format("Verifying %s equals %s", val1.toString(), val2.toString()));

            assertEquals(val1, val2, reason);
        }
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
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult("FALSE");
                setDoneFlag(true);
            }

        });
        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }
        assertThat("Database did not update correctly after trying to purchase out of stock items.", getResult(), is(PASS_FLAG));
    }

    @Test
    public void itPurchasesItemsFromStores() {
        final String SHOP_ID = "itPurchaseItemsFromStoresID";
        final String FIRST_ITEM_ID = "firstItemID";
        final String FIRST_ITEM_NAME = "firstItem";
        final int FIRST_ITEM_QTY = 30;
        final double FIRST_ITEM_COST = 4.99;
        final String SECOND_ITEM_ID = "secondItemID";
        final String SECOND_ITEM_NAME = "secondItem";
        final int SECOND_ITEM_QTY = 5;
        final double SECOND_ITEM_COST = 1.99;
        final int FIRST_ITEM_PURC = 3;
        final int SECOND_ITEM_PURC = 1;
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
                assertThat("Incorrect inventory", inventory, is(FIRST_ITEM_QTY - FIRST_ITEM_PURC));

                Map<String, Object> secondItemData = (Map<String, Object>) curStoreData.get(SECOND_ITEM_ID);
                Long secInvVal = (Long) secondItemData.get("inventory");
                int secInventory = secInvVal != null ? secInvVal.intValue() : null;
                assertThat("Incorrect inventory", secInventory, is(SECOND_ITEM_QTY - SECOND_ITEM_PURC));
                setResult(PASS_FLAG);
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult("FALSE");
                setDoneFlag(true);
            }

        });
        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }
        assertThat("Database did not update correctly after purchasing items.", getResult(), is(PASS_FLAG));
    }

    @Test
    public void itDoesntCreateUserWithSameName() {
        final String USERNAME = "aSpecificUsername";
        final String USER_ID = "aExistingUserID";

        DatabaseReference userRef = testDbInstance.getReference("test/users/customers/" + USER_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("address", "123 Some Road");
        map.put("email", "b@b.com");
        map.put("note", "A note.");
        map.put("password", "shhhhh");
        map.put("phoneNum", "0987654321");
        map.put("userName", USERNAME);
        userRef.updateChildrenAsync(map);
        firebaseDelay();

        HashMap<String, String> param = new HashMap<>();
        param.put("userName", USERNAME);
        param.put("password", "aPassword");
        param.put("email", "a@a.com");
        param.put("address", "Carleton");
        param.put("phoneNumber", "1234567890");
        param.put("note", "Some note.");
        firebaseDelay();

        try {
            functionCaller.sendPost(CloudTestController.createCustomer, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/users/customers/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot customers : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) customers.getValue();
                    String userName = (String) data.get("userName");
                    if (USERNAME.equals(userName)) {
                        setResult(customers.getKey());
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }
        assertThat("Customer was not created correctly.", getResult(), is(USER_ID));
    }

    @Test
    public void itCreatesACustomer() {
        final String USERNAME = "aUniqueUsername";
        final String NOTE = "thisIsANote";
        HashMap<String, String> param = new HashMap<>();
        param.put("userName", USERNAME);
        param.put("password", "aPassword");
        param.put("email", "a@a.com");
        param.put("address", "Carleton");
        param.put("phoneNumber", "1234567890");
        param.put("note", NOTE);

        try {
            functionCaller.sendPost(CloudTestController.createCustomer, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/users/customers/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot customers : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) customers.getValue();

                    String userName = (String) data.get("userName");
                    if (USERNAME.equals(userName)) {
                        assertThat("Incorrect username", userName, is(USERNAME));
                        String note = (String) data.get("note");
                        assertThat("Incorrect note", note, is(NOTE));
                        setResult(USERNAME);
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }
        assertThat("Customer was not created correctly.", getResult(), is(USERNAME));
    }


    public static void firebaseDelay() {
        try {
            Thread.sleep(5000);
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

    @AfterAll
    public static void cleanup() {
        testDbInstance.getReference("test/").removeValueAsync();
        firebaseDelay();
        testDbInstance = null;
        functionCaller = null;

    }

    @BeforeEach
    public void resetResult() {
        result = "";
        DONE_FLAG = false;
        DELAY_COUNTER = 0;
    }

    public void setResult(String res) {
        result = res;
    }

    public String getResult() {
        return result;
    }

    private void setDoneFlag(boolean newVal) {
        DONE_FLAG = newVal;
    }

    private boolean getDoneFlag() {
        return DONE_FLAG;
    }

}
