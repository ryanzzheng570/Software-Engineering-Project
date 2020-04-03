package CloudFunctionTest;

import ShopifyProj.Controller.CloudTestController;
import ShopifyProj.Controller.FirebaseController;

import com.google.firebase.database.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;


import java.util.*;

public class MerchantServicesTest {
    private static FirebaseDatabase testDbInstance = null;
    private static CloudTestController functionCaller = null;
    private String result = "";

    private final String PASS_FLAG = "PASS";
    private final String FAIL_FLAG = "FAIL";

    private boolean DONE_FLAG = false;
    private int DELAY_COUNTER = 0;
    private final int MAX_DELAYS = 5;

    private void setDoneFlag(boolean newVal) {
        DONE_FLAG = newVal;
    }

    private boolean getDoneFlag() {
        return DONE_FLAG;
    }

    private String changeShopName(String shopId, String newName) {
        String key = "";

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", shopId);
        param.put("shopName", newName);

        try {
            key = functionCaller.sendPost(CloudTestController.changeShopName, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    private String merchantLogin(String username, String password) {
        String key = "";
        HashMap<String, String> param = new HashMap<>();

        param.put("userName", username);
        param.put("password", password);

        try {
            key = functionCaller.sendPost(CloudTestController.merchantLogin, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;

    }

    private String createMerchant(String username, String password, String email, String phoneNumber) {
        String key = "";
        HashMap<String, String> param = new HashMap<>();

        param.put("userName", username);
        param.put("password", password);
        param.put("email", email);
        param.put("contactPhoneNumber", phoneNumber);

        try {
            key = functionCaller.sendPost(CloudTestController.createMerchant, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    private String createShop(String merchantId, String shopName) {
        String key = "";
        HashMap<String, String> param = new HashMap<>();

        param.put("shopName", shopName);
        param.put("userId", merchantId);

        try {
            key = functionCaller.sendPost(CloudTestController.addShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    private String deleteShop(String shopId, String merchantId) {
        String key = "";
        HashMap<String, String> param = new HashMap<>();

        param.put("shopID", shopId);
        param.put("userId", merchantId);

        try {
            key = functionCaller.sendPost(CloudTestController.deleteShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    private String addTagToShop(String shopId, String tagName) {
        String key = "";

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", shopId);
        param.put("tagName", tagName);

        try {
            key = functionCaller.sendPost(CloudTestController.addTagToShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    private String removeTagFromShop(String shopId, String tagId) {
        String key = "";
        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", shopId);
        param.put("tagID", tagId);

        try {
            key = functionCaller.sendPost(CloudTestController.removeTagFromShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    private String addItemToShop(String shopId, String url, String altText, String itemName, String cost, String inventory) {
        String key = "";

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", shopId);
        param.put("url", url);
        param.put("altText", altText);
        param.put("itemName", itemName);
        param.put("cost", cost);
        param.put("inventory", inventory);

        try {
            key = functionCaller.sendPost(CloudTestController.addItemToShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    private String removeItemFromShop(String shopId, String itemId) {
        String key = "";

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", shopId);
        param.put("itemID", itemId);

        try {
            key = functionCaller.sendPost(CloudTestController.removeItemFromShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    private String editItemInShop(String shopId, String itemId, String merchantId, String url, String altText, String itemName, String cost, String inventory) {
        String key = "";

        HashMap<String, String> param = new HashMap<>();
        param.put("shopID", shopId);
        param.put("itemId", itemId);
        param.put("merchantId", merchantId);
        param.put("url", url);
        param.put("altText", altText);
        param.put("itemName", itemName);
        param.put("cost", cost);
        param.put("inventory", inventory);
        firebaseDelay();

        try {
            key = functionCaller.sendPost(CloudTestController.editItemInShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    @Test
    public void itDoesntCreateMerchantWithTheSameUsername() {
        final String USERNAME = "aUniqueMerchantUsername";
        final String USER_ID = "aUniqueExistingUserID";

        DatabaseReference merchRef = testDbInstance.getReference("test/users/merchants/" + USER_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("email", "b@b.com");
        map.put("password", "shhhhh");
        map.put("phoneNum", "0987654321");
        map.put("userName", USERNAME);
        merchRef.updateChildrenAsync(map);
        firebaseDelay();

        createMerchant(USERNAME, "shhh", "b@b.com", "1234567890");
        firebaseDelay();

        testDbInstance.getReference("test/users/merchants/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot merchants : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) merchants.getValue();
                    String userName = (String) data.get("userName");
                    if (USERNAME.equals(userName)) {
                        setResult(merchants.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
            }

        });
        firebaseDelay();
        assertThat("Customer was not created correctly.", getResult(), is(USER_ID));
    }

    @Test
    public void itDoesntAddTagWithExistingName() {
        final String FIRST_SHOP_ID = "itDoesntAddTagWithExistingNameID";
        final String FIRST_SHOP_NAME = "itDoesntAddTagWithExistingName";
        final String MERCHANT_ID = "itDoesntAddTagWithExistingNameMerchantID";
        final String FIRST_SHOP_GENERATED_TOKEN = "TOK1";
        final String TAG_ID = "aTagID";
        final String TAG_NAME = "aTag";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference shop1Ref = testDbInstance.getReference("test/store/" + FIRST_SHOP_ID);
        Map<String, Object> shop1Map = new HashMap<>();
        shop1Map.put("shopName", FIRST_SHOP_NAME);
        shop1Ref.updateChildrenAsync(shop1Map);
        firebaseDelay();

        DatabaseReference tagRef = testDbInstance.getReference("test/store/" + FIRST_SHOP_ID + "/tag/");
        Map<String, Object> tagMap = new HashMap<>();
        tagMap.put(TAG_ID, TAG_NAME);
        tagRef.updateChildrenAsync(tagMap);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> rel1Map = new HashMap<>();
        rel1Map.put(FIRST_SHOP_GENERATED_TOKEN, FIRST_SHOP_ID);
        merchRel.updateChildrenAsync(rel1Map);
        firebaseDelay();

        addTagToShop(FIRST_SHOP_ID, TAG_NAME);
        firebaseDelay();

        ArrayList<String> shopNames = new ArrayList<String>();
        testDbInstance.getReference("test/store/" + FIRST_SHOP_ID + "/tag").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tagData : dataSnapshot.getChildren()) {
                    String tagName = (String) tagData.getValue();
                    if (TAG_NAME.equals(tagName)) {
                        setResult(tagData.getKey());
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        assertEquals(getResult(), TAG_ID, "Unexpected error adding a tag with the same name.");
    }

    @Test
    public void itDoesntEditItemWithTheExistingName() {
        final String MERCHANT_ID = "aMerchantID";
        final String ITEM_ID = "aItemID";
        final String SHOP_ID = "itDoesntEditItemWithTheExistingName";
        final String URL = "http://google.ca";
        final String ALT_TEXT = "Google";
        final String ITEM_NAME = "anItemName";
        final String COST = "5.99";
        final String INVENTORY = "20";
        final String GENERATED_TOKEN = "GenerateToken";
        final String SECOND_ITEM_ID = "aItemID";
        final String SECOND_ITEM_COST = "1.99";
        final String SECOND_ITEM_NAME = "anItemName2";
        final String SECOND_ITEM_INV = "1000";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", ITEM_NAME);
        map.put("cost", COST);
        map.put("inventory", INVENTORY);
        map.put("altText", ALT_TEXT);
        map.put("url", URL);
        firstItemRef.updateChildrenAsync(map);
        firebaseDelay();

        DatabaseReference secondItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + SECOND_ITEM_ID);
        Map<String, Object> secondmap = new HashMap<>();
        secondmap.put("name", SECOND_ITEM_NAME);
        secondmap.put("cost", SECOND_ITEM_COST);
        secondmap.put("inventory", SECOND_ITEM_INV);
        secondItemRef.updateChildrenAsync(secondmap);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> relMap = new HashMap<>();
        relMap.put(GENERATED_TOKEN, SHOP_ID);
        merchRel.updateChildrenAsync(relMap);

        firebaseDelay();
        editItemInShop(SHOP_ID, SECOND_ITEM_ID, MERCHANT_ID, URL, ALT_TEXT, ITEM_NAME, SECOND_ITEM_COST, SECOND_ITEM_INV);

        firebaseDelay();
        testDbInstance.getReference("test/store/" + SHOP_ID + "/item/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemID : dataSnapshot.getChildren()) {
                        Map<String, Object> currItemData = (Map<String, Object>) itemID.getValue();
                        String itemName = (String) currItemData.get("name");
                        if (ITEM_NAME.equals(itemName)) {
                            setResult(itemID.getKey());
                        }
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        assertEquals(getResult(), ITEM_ID, "There was an error trying to add an item with the same name.");
    }

    @Test
    public void itDoesntAddItemToShopWithExistingName() {

        final String MERCHANT_ID = "aMerchantID";
        final String ITEM_ID = "aItemID";
        final String SHOP_ID = "itDoesntAddItemToShopWithExistingNameID";
        final String URL = "http://google.ca";
        final String ALT_TEXT = "Google";
        final String ITEM_NAME = "anItemName";
        final String COST = "5.99";
        final String INVENTORY = "20";
        final String GENERATED_TOKEN = "GenerateToken";
        final String NEW_URL = "http://apple.ca";
        final String NEW_ALT_TEXT = "Apple";
        final String NEW_COST = "5.99";
        final String NEW_INVENTORY = "20";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", ITEM_NAME);
        map.put("cost", COST);
        map.put("inventory", INVENTORY);
        map.put("altText", ALT_TEXT);
        map.put("url", URL);
        firstItemRef.updateChildrenAsync(map);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> relMap = new HashMap<>();
        relMap.put(GENERATED_TOKEN, SHOP_ID);
        merchRel.updateChildrenAsync(relMap);
        firebaseDelay();

        addItemToShop(SHOP_ID, NEW_URL, NEW_ALT_TEXT, ITEM_NAME, NEW_COST, NEW_INVENTORY);

        firebaseDelay();
        testDbInstance.getReference("test/store/" + SHOP_ID + "/item/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot itemID : dataSnapshot.getChildren()) {
                        Map<String, Object> currItemData = (Map<String, Object>) itemID.getValue();
                        String itemName = (String) currItemData.get("name");
                        if (ITEM_NAME.equals(itemName)) {
                            setResult(itemID.getKey());
                        }
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        assertEquals(getResult(), ITEM_ID, "There was an error trying to add an item with the same name.");
    }

    @Test
    public void itDoesntEditShopWithExistingName() {
        final String FIRST_SHOP_ID = "firstShopID";
        final String FIRST_SHOP_NAME = "firstShop";
        final String MERCHANT_ID = "aMerchantID";
        final String FIRST_SHOP_GENERATED_TOKEN = "TOK1";
        final String EXISTING_SHOP_NAME = "iExist";
        final String EXISTING_SHOP_ID = "iExistID";


        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference shop1Ref = testDbInstance.getReference("test/store/" + FIRST_SHOP_ID);
        Map<String, Object> shop1Map = new HashMap<>();
        shop1Map.put("shopName", FIRST_SHOP_NAME);
        shop1Ref.updateChildrenAsync(shop1Map);
        firebaseDelay();

        DatabaseReference shop2Ref = testDbInstance.getReference("test/store/" + EXISTING_SHOP_ID);
        Map<String, Object> shop2Map = new HashMap<>();
        shop2Map.put("shopName", EXISTING_SHOP_NAME);
        shop2Ref.updateChildrenAsync(shop2Map);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> rel1Map = new HashMap<>();
        rel1Map.put(FIRST_SHOP_GENERATED_TOKEN, FIRST_SHOP_ID);
        merchRel.updateChildrenAsync(rel1Map);
        firebaseDelay();

        changeShopName(FIRST_SHOP_ID, EXISTING_SHOP_NAME);
        firebaseDelay();

        testDbInstance.getReference("test/store/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                    String currName = (String) ((Map<String, Object>) storeData.getValue()).get("shopName");
                    if (FIRST_SHOP_NAME.equals(currName)) {
                        setResult(storeData.getKey());
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        assertEquals(getResult(), FIRST_SHOP_ID, "Incorrect store ID, meaning something went wrong when trying to edit the shop.");
    }

    @Test
    public void itDoesntCreateShopWithExistingName() {
        final String FIRST_SHOP_ID = "itDoesntCreateShopWithExistingNamefirstShopID";
        final String FIRST_SHOP_NAME = "itDoesntCreateShopWithExistingNamefirstShop";
        final String MERCHANT_ID = "itDoesntCreateShopWithExistingNameaMerchantID";
        final String FIRST_SHOP_GENERATED_TOKEN = "TOK1";


        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference shop1Ref = testDbInstance.getReference("test/store/" + FIRST_SHOP_ID);
        Map<String, Object> shop1Map = new HashMap<>();
        shop1Map.put("shopName", FIRST_SHOP_NAME);
        shop1Ref.updateChildrenAsync(shop1Map);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> rel1Map = new HashMap<>();
        rel1Map.put(FIRST_SHOP_GENERATED_TOKEN, FIRST_SHOP_ID);
        merchRel.updateChildrenAsync(rel1Map);
        firebaseDelay();

        createShop(MERCHANT_ID, FIRST_SHOP_NAME);
        firebaseDelay();

        testDbInstance.getReference("test/store/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                    String currName = (String) ((Map<String, Object>) storeData.getValue()).get("shopName");
                    if (FIRST_SHOP_NAME.equals(currName)) {
                        setResult(storeData.getKey());
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        assertEquals(getResult(), FIRST_SHOP_ID, "Incorrect store ID, meaning something went wrong when trying to create the shop.");
    }

    @Test
    public void itEditsAnItemInAShop() {
        final String SHOP_ID = "itEditsAItemInAShopId";
        final String MERCHANT_ID = "aMerchantId";
        final String GENERATED_TOKEN = "GenereatedTokenForEditItemInShop";
        final double FIRST_ITEM_COST = 29.99;
        final int FIRST_ITEM_INV = 1000;
        final String FIRST_ITEM_URL = "FirstItemUrl";
        final String FIRST_ITEM_ALT_TEXT = "FirstItemAltText";
        final String FIRST_ITEM_NAME = "FirstItemNameInEditsShop";
        final String FIRST_ITEM_ID = "FirstItemIDInEditsShop";

        final String EDIT_ITEM_NAME = "editItemNameInShop";
        final double EDIT_ITEM_COST = 1.99;
        final int EDIT_ITEM_INV = 50;
        final String EDIT_ITEM_URL = "EditItemUrl";
        final String EDIT_ITEM_ALT_TEXT = "EditItemAltText";
        final String EDIT_ITEM_ID = "EditItemInShopID";

        final String EDITED_ITEM_COST = "0.49";
        final String EDITED_ITEM_INV = "5000";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> relMap = new HashMap<>();
        relMap.put(GENERATED_TOKEN, SHOP_ID);
        merchRel.updateChildrenAsync(relMap);
        firebaseDelay();

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + FIRST_ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", FIRST_ITEM_NAME);
        map.put("cost", FIRST_ITEM_COST);
        map.put("inventory", FIRST_ITEM_INV);
        map.put("altText", FIRST_ITEM_ALT_TEXT);
        map.put("url", FIRST_ITEM_URL);
        firstItemRef.updateChildrenAsync(map);
        firebaseDelay();

        DatabaseReference secItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + EDIT_ITEM_ID);
        Map<String, Object> secMap = new HashMap<>();
        secMap.put("name", EDIT_ITEM_NAME);
        secMap.put("cost", EDIT_ITEM_COST);
        secMap.put("inventory", EDIT_ITEM_INV);
        secMap.put("url", EDIT_ITEM_URL);
        secMap.put("altText", EDIT_ITEM_ALT_TEXT);
        secItemRef.updateChildrenAsync(secMap);
        firebaseDelay();
        editItemInShop(SHOP_ID, EDIT_ITEM_ID, MERCHANT_ID, EDIT_ITEM_URL, EDIT_ITEM_ALT_TEXT, EDIT_ITEM_NAME, EDITED_ITEM_COST, EDITED_ITEM_INV);

        firebaseDelay();
        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();
        testDbInstance.getReference("test/store/" + SHOP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> curStoreData = (Map<String, Object>) ((Map<String, Object>) dataSnapshot.getValue()).get("item");

                Map<String, Object> firstItemData = (Map<String, Object>) curStoreData.get(FIRST_ITEM_ID);
                Long invVal = (Long) firstItemData.get("inventory");
                int inventory = invVal != null ? invVal.intValue() : null;
                toVerify.put("Incorrect inventory for first item", new Object[]{inventory, FIRST_ITEM_INV});
                double cost;
                try {
                    cost = (double) firstItemData.get("cost");
                } catch (Exception f) {
                    String costVal = (String) firstItemData.get("cost");
                    cost = Double.parseDouble(costVal);
                }
                toVerify.put("Incorrect cost for first item", new Object[]{cost, FIRST_ITEM_COST});


                Map<String, Object> secondItemData = (Map<String, Object>) curStoreData.get(EDIT_ITEM_ID);
                invVal = (Long) secondItemData.get("inventory");
                inventory = invVal != null ? invVal.intValue() : null;
                toVerify.put("Incorrect inventory for edited item", new Object[]{inventory, Integer.parseInt(EDITED_ITEM_INV)});
                try {
                    cost = (double) secondItemData.get("cost");
                } catch (Exception f) {
                    String costVal = (String) secondItemData.get("cost");
                    cost = Double.parseDouble(costVal);
                }
                toVerify.put("Incorrect cost for edited item", new Object[]{cost, Double.parseDouble(EDITED_ITEM_COST)});
                setResult(PASS_FLAG);
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
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

            assertEquals(val2, val1, reason);

        }
        assertEquals(getResult(), PASS_FLAG, "onDataChange did not run,");

    }

    @Test
    public void itRemovesAnItemFromAShop() {
        final String SHOP_ID = "itRemovesAItemFromAShopID";
        final String MERCHANT_ID = "aMerchantId";
        final String GENERATED_TOKEN = "GenereatedToken";
        final String FIRST_ITEM_COST = "29.99";
        final String FIRST_ITEM_INV = "1000";
        final String FIRST_ITEM_NAME = "FirstItemName";
        final String FIRST_ITEM_ID = "FirstItemID";
        final String REMOVE_ITEM_COST = "9.99";
        final String REMOVE_ITEM_INV = "5000";
        final String REMOVE_ITEM_NAME = "REMOVEItemName";
        final String REMOVE_ITEM_ID = "REMOVEItemID";
        final String THIRD_ITEM_COST = "0.99";
        final String THIRD_ITEM_INV = "5";
        final String THIRD_ITEM_NAME = "THIRDItemName";
        final String THIRD_ITEM_ID = "THIRDItemID";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        firebaseDelay();
        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> relMap = new HashMap<>();
        relMap.put(GENERATED_TOKEN, SHOP_ID);
        merchRel.updateChildrenAsync(relMap);
        firebaseDelay();

        DatabaseReference firstItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + FIRST_ITEM_ID);
        Map<String, Object> map = new HashMap<>();
        map.put("name", FIRST_ITEM_NAME);
        map.put("cost", FIRST_ITEM_COST);
        map.put("inventory", FIRST_ITEM_INV);
        firstItemRef.updateChildrenAsync(map);
        firebaseDelay();

        DatabaseReference secItemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + REMOVE_ITEM_ID);
        Map<String, Object> secMap = new HashMap<>();
        secMap.put("name", REMOVE_ITEM_NAME);
        secMap.put("cost", REMOVE_ITEM_COST);
        secMap.put("inventory", REMOVE_ITEM_INV);
        secItemRef.updateChildrenAsync(secMap);
        firebaseDelay();

        DatabaseReference thirdtemRef = testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + THIRD_ITEM_ID);
        Map<String, Object> thirdMap = new HashMap<>();
        thirdMap.put("name", THIRD_ITEM_NAME);
        thirdMap.put("cost", THIRD_ITEM_COST);
        thirdMap.put("inventory", THIRD_ITEM_INV);
        thirdtemRef.updateChildrenAsync(thirdMap);
        firebaseDelay();

        removeItemFromShop(SHOP_ID, REMOVE_ITEM_ID);
        firebaseDelay();

        ArrayList<String> toVerify = new ArrayList<String>();
        testDbInstance.getReference("test/store/" + SHOP_ID + "/item/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                    toVerify.add(storeData.getKey());
                    setResult(PASS_FLAG);
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        for (String id : toVerify) {
            System.out.println(String.format("Verifying %s does not equal %s", id, REMOVE_ITEM_ID));

            assertNotEquals("Item was not deleted", id, REMOVE_ITEM_ID);
        }

        assertEquals(getResult(), PASS_FLAG, "OnDataChange method did not run");
    }

    @Test
    public void itAddsAnItemToAShop() {
        final String MERCHANT_ID = "aMerchantID";
        final String SHOP_ID = "itAddsAnItemToAShopID";
        final String URL = "http://google.ca";
        final String ALT_TEXT = "Google";
        final String ITEM_NAME = "anItemName";
        final String COST = "5.99";
        final String INVENTORY = "20";
        final int INT_INV = 20;
        final String GENERATED_TOKEN = "GenerateToken";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> relMap = new HashMap<>();
        relMap.put(GENERATED_TOKEN, SHOP_ID);
        merchRel.updateChildrenAsync(relMap);
        firebaseDelay();

        addItemToShop(SHOP_ID, URL, ALT_TEXT, ITEM_NAME, COST, INVENTORY);

        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();
        firebaseDelay();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/item").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot itemIDs : dataSnapshot.getChildren()) {
                    Map<String, Object> currItemData = (Map<String, Object>) itemIDs.getValue();
                    String itemName = (String) currItemData.get("name");
                    if (ITEM_NAME.equals(itemName)) {
                        toVerify.put("Incorrect name", new Object[]{itemName, ITEM_NAME});

                        String cost = ((String) currItemData.get("cost"));
                        toVerify.put("Incorrect cost", new Object[]{cost, COST});

                        Long invVal = (Long) currItemData.get("inventory");
                        int inventory = invVal != null ? invVal.intValue() : null;
                        toVerify.put("Incorrect inventory", new Object[]{inventory, INT_INV});

                        String url = (String) currItemData.get("url");
                        toVerify.put("Incorrect URL", new Object[]{url, URL});

                        String altText = (String) currItemData.get("altText");
                        toVerify.put("Incorrect alt text", new Object[]{altText, ALT_TEXT});

                        setResult(PASS_FLAG);
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
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

        assertEquals(getResult(), PASS_FLAG, "OnDataChange method did not run");
    }

    @Test
    public void itRemovesATagFromAShop() {
        final String SHOP_ID = "itRemovesATagFromAShopID";
        final String FIRST_TAG_NAME = "firstTag";
        final String FIRST_TAG_ID = "firstTagID";
        final String REMOVE_TAG_NAME = "removeTag";
        final String REMOVE_TAG_ID = "removeTagID";
        final String THIRD_TAG_NAME = "thirdTag";
        final String THIRD_TAG_ID = "thirdTagID";
        final String MERCHANT_ID = "aMerchantID";
        final String GENERATED_TOKEN = "GeneratedToken";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> relMap = new HashMap<>();
        relMap.put(GENERATED_TOKEN, SHOP_ID);
        merchRel.updateChildrenAsync(relMap);
        firebaseDelay();

        DatabaseReference tagRel = testDbInstance.getReference("test/store/" + SHOP_ID + "/tag");
        Map<String, Object> tag1Map = new HashMap<>();
        tag1Map.put(FIRST_TAG_ID, FIRST_TAG_NAME);
        tagRel.updateChildrenAsync(tag1Map);
        firebaseDelay();

        Map<String, Object> tag2Map = new HashMap<>();
        tag2Map.put(REMOVE_TAG_ID, REMOVE_TAG_NAME);
        tagRel.updateChildrenAsync(tag2Map);
        firebaseDelay();

        Map<String, Object> tag3Map = new HashMap<>();
        tag3Map.put(THIRD_TAG_ID, THIRD_TAG_NAME);
        tagRel.updateChildrenAsync(tag3Map);
        firebaseDelay();

        removeTagFromShop(SHOP_ID, REMOVE_TAG_ID);
        firebaseDelay();


        ArrayList<String> toVerify = new ArrayList<String>();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/tag/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                    toVerify.add(storeData.getKey());
                    setResult(PASS_FLAG);
                    setDoneFlag(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        for (String id : toVerify) {
            System.out.println(String.format("Verifying %s does not equal %s", id, REMOVE_TAG_ID));

            assertNotEquals("Item was not deleted", id, REMOVE_TAG_ID);
        }

        assertEquals(getResult(), PASS_FLAG, "OnDataChange method did not run");
    }

    @Test
    public void itAddsATagToAShop() {
        final String SHOP_ID = "itAddsATagToAShopID";
        final String TAG_NAME = "aTagNameForAShop";
        final String GENERATED_TOKEN = "GeneratedTokenForAddingTag";
        final String MERCHANT_ID = "aMerchantIDToAddATag";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aTagUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> relMap = new HashMap<>();
        relMap.put(GENERATED_TOKEN, SHOP_ID);
        merchRel.updateChildrenAsync(relMap);
        firebaseDelay();

        addTagToShop(SHOP_ID, TAG_NAME);
        firebaseDelay();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/tag/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tags : dataSnapshot.getChildren()) {
                    String tagName = (String) tags.getValue();
                    if (TAG_NAME.equals(tagName)) {
                        setResult(TAG_NAME);
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        assertEquals(getResult(), TAG_NAME, "Tag was not successfully added.");
    }

    @Test
    public void itCreatesAShop() {
        final String MERCHANT_ID = "aMerchantIDToCreateAShop";
        final String SHOP_NAME = "itCreatesAShopTestName";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "createdShopUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        createShop(MERCHANT_ID, SHOP_NAME);
        firebaseDelay();

        testDbInstance.getReference("test/store").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeID : dataSnapshot.getChildren()) {
                    String shopName = (String) ((Map<String, Object>) storeID.getValue()).get("shopName");
                    if (SHOP_NAME.equals(shopName)) {
                        setResult(storeID.getKey());
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        ArrayList<String> toVerify2 = new ArrayList<String>();
        setDoneFlag(false);
        DELAY_COUNTER = 0;

        firebaseDelay();
        testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> temp = (Map<String, Object>) dataSnapshot.getValue();

                Set<String> keys = temp.keySet();

                for (String key : keys) {
                    toVerify2.add((String) temp.get(key));
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }
        assertEquals(toVerify2.contains(getResult()), true, "The shop was not created properly.");
    }

    @Test
    public void itDeletesAShop() {
        final String FIRST_SHOP_ID = "itDeletesAShopfirstShopID";
        final String FIRST_SHOP_NAME = "itDeletesAShopfirstShop";
        final String DELETE_SHOP_NAME = "itDeletesAShopdeleteShop";
        final String DELETE_SHOP_ID = "itDeletesAShopdeleteShopID";
        final String THIRD_SHOP_ID = "itDeletesAShopthirdShopID";
        final String THIRD_SHOP_NAME = "itDeletesAShopthirdShop";
        final String MERCHANT_ID = "itDeletesAShopaMerchantID";
        final String FIRST_SHOP_GENERATED_TOKEN = "TOK1";
        final String DELETE_SHOP_GENERATED_TOKEN = "TOK2";
        final String THIRD_SHOP_GENERATED_TOKEN = "TOK3";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference shop1Ref = testDbInstance.getReference("test/store/" + FIRST_SHOP_ID);
        Map<String, Object> shop1Map = new HashMap<>();
        shop1Map.put("shopName", FIRST_SHOP_NAME);
        shop1Ref.updateChildrenAsync(shop1Map);
        firebaseDelay();

        DatabaseReference shop2Ref = testDbInstance.getReference("test/store/" + DELETE_SHOP_ID);
        Map<String, Object> shop2Map = new HashMap<>();
        shop2Map.put("shopName", DELETE_SHOP_NAME);
        shop2Ref.updateChildrenAsync(shop2Map);
        firebaseDelay();

        DatabaseReference shop3Ref = testDbInstance.getReference("test/store/" + THIRD_SHOP_ID);
        Map<String, Object> shop3Map = new HashMap<>();
        shop3Map.put("shopName", THIRD_SHOP_NAME);
        shop3Ref.updateChildrenAsync(shop3Map);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> rel1Map = new HashMap<>();
        rel1Map.put(FIRST_SHOP_GENERATED_TOKEN, FIRST_SHOP_ID);
        merchRel.updateChildrenAsync(rel1Map);
        firebaseDelay();

        DatabaseReference merchRel2 = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> rel2Map = new HashMap<>();
        rel2Map.put(DELETE_SHOP_GENERATED_TOKEN, DELETE_SHOP_ID);
        merchRel2.updateChildrenAsync(rel2Map);
        firebaseDelay();

        DatabaseReference merchRel3 = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> rel3Map = new HashMap<>();
        rel3Map.put(THIRD_SHOP_GENERATED_TOKEN, THIRD_SHOP_ID);
        merchRel3.updateChildrenAsync(rel3Map);
        firebaseDelay();


        deleteShop(DELETE_SHOP_ID, MERCHANT_ID);
        firebaseDelay();

        ArrayList<String> shopNames = new ArrayList<String>();
        testDbInstance.getReference("test/store/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                    String currName = (String) ((Map<String, Object>) storeData.getValue()).get("shopName");
                    shopNames.add(currName);
                    setResult(PASS_FLAG);
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        assertEquals(shopNames.contains(DELETE_SHOP_NAME), false);
        assertEquals(getResult(), PASS_FLAG, "OnDataChange method did not run v1");

        setDoneFlag(false);
        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();
        DELAY_COUNTER = 0;
        setResult(FAIL_FLAG);
        testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> temp = (Map<String, Object>) dataSnapshot.getValue();

                Set<String> keys = temp.keySet();
                List<String> keyList = new ArrayList<String>();
                for (String key : keys) {
                    keyList.add((String) temp.get(key));
                }

                List<String> check = new ArrayList<String>();
                check.add(FIRST_SHOP_ID);
                check.add(THIRD_SHOP_ID);

                Collections.sort(check);
                Collections.sort(keyList);

                toVerify.put("No key removed.", new Object[]{keyList.size(), 2});
                toVerify.put("Wrong key removed.", new Object[]{keyList, check});

                setResult(PASS_FLAG);
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());

                setResult(FAIL_FLAG);
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

        assertEquals(getResult(), PASS_FLAG, "OnDataChange method did not runv2");
    }

    @Test
    public void itChangesAShopName() {
        final String SHOP_ID = "itChangesAShopNameID";
        final String ORIGINAL_NAME = "itChangesAShopName";
        final String NEW_NAME = "aBetterName";
        final String MERCHANT_ID = "aMerchantID";
        final String GENERATED_TOKEN = "GeneratedToken";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", "a@a.com");
        merchMap.put("password", "aPassword");
        merchMap.put("phoneNum", "(xxx)xxx-xxxx");
        merchMap.put("userName", "aUsername");
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        DatabaseReference shop1Ref = testDbInstance.getReference("test/store/" + SHOP_ID);
        Map<String, Object> shop1Map = new HashMap<>();
        shop1Map.put("shopName", ORIGINAL_NAME);
        shop1Ref.updateChildrenAsync(shop1Map);
        firebaseDelay();

        DatabaseReference merchRel = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops");
        Map<String, Object> rel1Map = new HashMap<>();
        rel1Map.put(GENERATED_TOKEN, SHOP_ID);
        merchRel.updateChildrenAsync(rel1Map);
        firebaseDelay();

        changeShopName(SHOP_ID, NEW_NAME);
        firebaseDelay();

        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();
        testDbInstance.getReference("test/store/" + SHOP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String shopName = (String) ((Map<String, Object>) dataSnapshot.getValue()).get("shopName");
                toVerify.put("Store name was not updated.", new Object[]{shopName, NEW_NAME});
                setResult(PASS_FLAG);
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());

                setResult(FAIL_FLAG);
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
        assertEquals(getResult(), PASS_FLAG, "OnDataChange method did not run");
    }

    @Test
    public void itCreatesAMerchant() {
        final String USERNAME = "aUniqueUsernameToCreate";
        String password = "aPassword";
        String email = "a@a.com";
        String phoneNum = "1234567890";
        createMerchant(USERNAME, password, email, phoneNum);
        firebaseDelay();

        testDbInstance.getReference("test/users/merchants/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot customers : dataSnapshot.getChildren()) {
                    Map<String, Object> data = (Map<String, Object>) customers.getValue();

                    String userName = (String) data.get("userName");
                    if (USERNAME.equals(userName)) {
                        assertThat("Incorrect username", userName, is(USERNAME));
                        setResult(USERNAME);
                    }
                }
                setDoneFlag(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fail("The database read failed: " + databaseError.getCode());
                setResult(FAIL_FLAG);
                setDoneFlag(true);
            }

        });

        while (!getDoneFlag() && DELAY_COUNTER++ < MAX_DELAYS) {
            firebaseDelay();
        }

        assertEquals(getResult(), USERNAME, "OnDataChange method did not run");
    }

    @Test
    public void itLogsInCorrectly() {
        final String USERNAME = "aUsername";
        final String PASSWORD = "aPassword";
        String email = "a@a.com";
        String phoneNum = "1234567890";

        final String MERCHANT_ID = "aMerchantID";

        DatabaseReference merchantRef = testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID);
        Map<String, Object> merchMap = new HashMap<>();
        merchMap.put("email", email);
        merchMap.put("password", PASSWORD);
        merchMap.put("phoneNum", phoneNum);
        merchMap.put("userName", USERNAME);
        merchantRef.updateChildrenAsync(merchMap);
        firebaseDelay();

        String MERCHANT_LOGIN_ID = merchantLogin(USERNAME, PASSWORD);
        firebaseDelay();

        assertNotEquals(MERCHANT_LOGIN_ID, functionCaller.getCallErrorMsg(), "Could not find the store in the DB.");
    }

    public static void firebaseDelay() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            fail("waitForCloudFunction exception\n" + ex.getStackTrace().toString());
        }
    }

    @BeforeAll
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
}
