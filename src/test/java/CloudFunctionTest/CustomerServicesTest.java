package CloudFunctionTest;

import ShopifyProj.Controller.CloudTestController;
import ShopifyProj.Controller.FirebaseController;

import com.google.firebase.database.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


import java.util.HashMap;
import java.util.Map;

public class CustomerServicesTest {
    private static FirebaseDatabase testDbInstance = null;
    private static CloudTestController functionCaller = null;

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

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + FIRST_ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", FIRST_ITEM_NAME);
        map.put("cost", FIRST_ITEM_COST);
        map.put("inventory", FIRST_ITEM_QTY);
        firstItemRef.updateChildrenAsync(map);
        firebaseDelay();

        DatabaseReference secItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + SECOND_ITEM_ID);
        Map<String, Object> secMap = new HashMap<>();
        secMap.put("name", SECOND_ITEM_NAME);
        secMap.put("cost", SECOND_ITEM_COST);
        secMap.put("inventory", SECOND_ITEM_QTY);
        secItemRef.updateChildrenAsync(secMap);

        firebaseDelay();
        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", SHOP_ID);
        param.put("itemIDs",  SECOND_ITEM_ID);
        param.put("quantities", SECOND_ITEM_PURC);

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
                assertThat("Incorrect inventory", inventory, is(Integer.parseInt(FIRST_ITEM_QTY)));

                Map<String, Object> secondItemData = (Map<String, Object>) curStoreData.get(FIRST_ITEM_ID);
                Long secInvVal = (Long) secondItemData.get("inventory");
                int secInventory = secInvVal != null ? secInvVal.intValue() : null;
                assertThat("Incorrect inventory", secInventory, is(Integer.parseInt(SECOND_ITEM_QTY)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        // Need second delay because slow update of cloud function
        firebaseDelay();
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


        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + FIRST_ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", FIRST_ITEM_NAME);
        map.put("cost", FIRST_ITEM_COST);
        map.put("inventory", FIRST_ITEM_QTY);
        firstItemRef.updateChildrenAsync(map);
        firebaseDelay();

        DatabaseReference secItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + SECOND_ITEM_ID);
        Map<String, Object> secMap = new HashMap<>();
        secMap.put("name", SECOND_ITEM_NAME);
        secMap.put("cost", SECOND_ITEM_COST);
        secMap.put("inventory", SECOND_ITEM_QTY);
        secItemRef.updateChildrenAsync(secMap);

        firebaseDelay();
        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", SHOP_ID);
        param.put("itemIDs", FIRST_ITEM_ID + ", " + SECOND_ITEM_ID);
        param.put("quantities", FIRST_ITEM_PURC + ", " + SECOND_ITEM_PURC);

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
                assertThat("Incorrect inventory", inventory, is(Integer.parseInt(FIRST_ITEM_QTY)- Integer.parseInt(FIRST_ITEM_PURC)));

                Map<String, Object> secondItemData = (Map<String, Object>) curStoreData.get(FIRST_ITEM_ID);
                Long secInvVal = (Long) secondItemData.get("inventory");
                int secInventory = secInvVal != null ? secInvVal.intValue() : null;
                assertThat("Incorrect inventory", secInventory, is(Integer.parseInt(SECOND_ITEM_QTY)- Integer.parseInt(SECOND_ITEM_PURC)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        // Need second delay because slow update of cloud function
        firebaseDelay();
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
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
        testDbInstance = FirebaseController.getTestInstance();
        functionCaller = new CloudTestController();
    }

    @AfterClass
    public static void cleanup() {
        testDbInstance.getReference("test/").removeValueAsync();
        firebaseDelay();
        testDbInstance = null;
        functionCaller = null;

    }

}
