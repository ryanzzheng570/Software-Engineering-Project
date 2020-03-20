package CloudFunctionTest;

import ShopifyProj.Controller.CloudServiceController;
import ShopifyProj.Controller.FirebaseController;

import ShopifyProj.Model.Shop;
import com.google.firebase.database.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


import java.util.HashMap;
import java.util.Map;

public class MerchantServicesTest {
    private static FirebaseDatabase testDbInstance = null;
    private static CloudServiceController functionCaller = null;
    private static String result = "";

    @Test
    public void itRemovesAItemFromAShop() {
        final String SHOP_ID = "itRemovesAItemFromAShopID";
        final String FIRST_ITEM_ID = "firstItemID";
        final String REMOVE_ITEM_ID = "removeItemID";
        final String THIRD_ITEM_ID = "thirdItemID";

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + FIRST_ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "FistItemName");
        firstItemRef.updateChildrenAsync(map);

        DatabaseReference secItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + REMOVE_ITEM_ID);
        Map<String, Object> secMap = new HashMap<>();
        secMap.put("name", "RemoveItemName");
        secItemRef.updateChildrenAsync(secMap);

        DatabaseReference thirdItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + THIRD_ITEM_ID);
        Map<String, Object> thrdMap = new HashMap<>();
        thrdMap.put("name", "ThirdItemName");
        thirdItemRef.updateChildrenAsync(thrdMap);

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", SHOP_ID);
        param.put("itemID", REMOVE_ITEM_ID);
        firebaseDelay();

        try {
            functionCaller.sendPost(CloudServiceController.removeItemFromShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/item/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                    assertThat("Item was not deleted", storeData.getKey(), is(not(REMOVE_ITEM_ID)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
    }

    @Test
    public void itAddsAItemToAShop() {
        final String SHOP_ID = "itAddsAItemToAShopID";
        final String NAME = "itAddsAItemToAShop";
        final String URL = "http://google.ca";
        final String ALT_TEXT = "Google";
        final String ITEM_NAME = "anItemName";
        final String COST = "5.99";
        final String INVENTORY = "20";
        String key = "";

        DatabaseReference ref = testDbInstance.getReference("test/store/");
        Map<String, Object> map = new HashMap<>();
        map.put(SHOP_ID, new Shop(NAME));
        ref.updateChildrenAsync(map);

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", SHOP_ID);
        param.put("url", URL);
        param.put("altText", ALT_TEXT);
        param.put("itemName", ITEM_NAME);
        param.put("cost", COST);
        param.put("inventory", INVENTORY);
        firebaseDelay();

        try {
            key = functionCaller.sendPost(CloudServiceController.addItemToShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> currItemData = (Map<String, Object>) dataSnapshot.getValue();

                String itemName = (String) currItemData.get("name");
                assertThat("Incorrect name", itemName, is(ITEM_NAME));

                String cost = ((String) currItemData.get("cost"));
                assertThat("Incorrect cost", cost, is(COST));

                Long invVal = (Long) currItemData.get("inventory");
                int inventory = invVal != null ? invVal.intValue() : null;
                assertThat("Incorrect inventory", inventory, is(INVENTORY));

                String url = (String) currItemData.get("url");
                assertThat("Incorrect URL", url, is(URL));

                String altText = (String) currItemData.get("altText");
                assertThat("Incorrect alt text", altText, is(ALT_TEXT));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
    }

    @Test
    public void itRemovesATagFromAShop() {
        final String SHOP_ID = "itRemovesATagFromAShopID";
        final String NAME = "itRemovesATagFromAShop";
        final String FIRST_TAG_NAME = "firstTag";
        final String REMOVE_TAG_NAME = "removeTag";
        final String THIRD_TAG_NAME = "thirdTag";
        final String FIRST_TAG_ID = "firstTagID";
        final String REMOVE_TAG_ID = "removeTagID";
        final String THIRD_TAG_ID = "thirdTagID";

        DatabaseReference storeRef = testDbInstance.getReference("test/store/");
        Map<String, Object> map = new HashMap<>();
        map.put(SHOP_ID, new Shop(NAME));
        storeRef.updateChildrenAsync(map);

        DatabaseReference tagRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/tag/");
        Map<String, Object> tagMap = new HashMap<>();
        tagMap.put(FIRST_TAG_ID, FIRST_TAG_NAME);
        tagMap.put(REMOVE_TAG_ID, REMOVE_TAG_NAME);
        tagMap.put(THIRD_TAG_ID, THIRD_TAG_NAME);
        tagRef.updateChildrenAsync(tagMap);

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", SHOP_ID);
        param.put("tagID", REMOVE_TAG_ID);
        firebaseDelay();

        try {
            functionCaller.sendPost(CloudServiceController.removeTagFromShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/tag/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                    assertThat("Store was not deleted", storeData.getKey(), is(not(REMOVE_TAG_ID)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
    }

    @Test
    public void itAddsATagToAShop() {
        final String SHOP_ID = "itAddsATagToAShopID";
        final String NAME = "itAddsATagToAShop";
        final String TAG_NAME = "aTagName";
        String key = "";

        DatabaseReference ref = testDbInstance.getReference("test/store/");
        Map<String, Object> map = new HashMap<>();
        map.put(SHOP_ID, new Shop(NAME));
        ref.updateChildrenAsync(map);

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", SHOP_ID);
        param.put("tagName", TAG_NAME);
        firebaseDelay();

        try {
            key = functionCaller.sendPost(CloudServiceController.addTagToShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/tag/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setResult((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        assertThat("Tag was not added to the store.", getResult(), is(TAG_NAME));

    }

    @Test
    public void itCreatesAShop() {

        final String SHOP_NAME = "itCreatesAShop";
        HashMap<String, String> param = new HashMap<>();
        param.put("shopName", SHOP_NAME);
        String key = "";

        try {
            key = functionCaller.sendPost(CloudServiceController.addShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/store/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setResult(((String) ((Map<String, Object>) dataSnapshot.getValue()).get("name")));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });

        firebaseDelay();
        assertThat("Could not find the store in the DB.", getResult(), is(SHOP_NAME));
    }

    @Test
    public void itDeletesAShop() {
        final String FIRST_SHOP_ID = "firstShopID";
        final String DELETE_SHOP_ID = "deleteShopID";
        final String THIRD_SHOP_ID = "thirdShopID";

        DatabaseReference ref = testDbInstance.getReference("test/store/");
        Map<String, Object> map = new HashMap<>();
        map.put(FIRST_SHOP_ID, new Shop("First Shop"));
        map.put(DELETE_SHOP_ID, new Shop("Second Shop"));
        map.put(THIRD_SHOP_ID, new Shop("Third Shop"));
        ref.updateChildrenAsync(map);

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", DELETE_SHOP_ID);
        firebaseDelay();

        try {
            functionCaller.sendPost(CloudServiceController.deleteShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/store/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                    assertThat("Store was not deleted", storeData.getKey(), is(not(DELETE_SHOP_ID)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
    }

    @Test
    public void itChangesAShopName() {
        final String SHOP_ID = "itChangesAShopNameID";
        final String ORIGNIAL_NAME = "itChangesAShopName";
        final String NEW_NAME = "aBetterName";

        DatabaseReference ref = testDbInstance.getReference("test/store/");
        Map<String, Object> map = new HashMap<>();
        map.put(SHOP_ID, new Shop(ORIGNIAL_NAME));
        ref.updateChildrenAsync(map);

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", SHOP_ID);
        param.put("shopName", NEW_NAME);
        firebaseDelay();

        try {
            functionCaller.sendPost(CloudServiceController.changeShopName, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/store/" + SHOP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setResult(((String) ((Map<String, Object>) dataSnapshot.getValue()).get("name")));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        assertThat("Store name was not updated.", getResult(), is(NEW_NAME));

    }

    @Test
    public void itCreatesAMerchant() {
        String key = "";
        final String USERNAME = "aUsername";
        HashMap<String, String> param = new HashMap<>();
        param.put("userName", USERNAME);
        param.put("password", "aPassword");
        param.put("email", "a@a.com");
        param.put("contactPhoneNumber", "1234567890");

        try {
            key = functionCaller.sendPost(CloudServiceController.createMerchant, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        testDbInstance.getReference("test/users/merchants/" + key + "/userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setResult((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        assertThat("Merchant was not added.", getResult(), is(USERNAME));
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
        functionCaller = new CloudServiceController();
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
