package CloudFunctionTest;

import ShopifyProj.Controller.CloudTestController;
import ShopifyProj.Controller.FirebaseController;

import ShopifyProj.Model.Shop;
import com.google.firebase.database.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


import java.util.*;

public class MerchantServicesTest {
    private static FirebaseDatabase testDbInstance = null;
    private static CloudTestController functionCaller = null;
    private static String result = "";

    private static final String PASS_FLAG = "PASS";
    private static final String FAIL_FLAG = "FAIL";

    private static boolean DONE_FLAG = false;
    private static int DELAY_COUNTER = 0;
    private static final int MAX_DELAYS = 3;

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

        try {
            key = functionCaller.sendPost(CloudTestController.editItemInShop, param);
        } catch (Exception e) {
            fail("sendPost exception");
        }
        firebaseDelay();

        return key;
    }

    @Test
    public void itEditsAnItemInAShop() {
        final String SHOP_NAME = "itEditsAItemInAShop";
        final String MERCHANT_NAME = "aMerchantId";

        final String FIRST_ITEM_COST = "29.99";
        final String FIRST_ITEM_INV = "1000";
        final String FIRST_ITEM_URL = "FirstItemUrl";
        final String FIRST_ITEM_ALT_TEXT = "FirstItemAltText";
        final String FIRST_ITEM_NAME = "FirstItemName";

        final String EDIT_ITEM_NAME = "editItemName";
        final String EDIT_ITEM_COST = "1.99";
        final String EDIT_ITEM_INV = "50";
        final String EDIT_ITEM_URL = "EditItemUrl";
        final String EDIT_ITEM_ALT_TEXT = "EditItemAltText";

        final String EDITED_ITEM_COST = "0.49";
        final String EDITED_ITEM_INV = "5000";

        String MERCHANT_ID = createMerchant(MERCHANT_NAME, "aPassword", "a@a.com", "(xxx)xxx-xxxx");
        String SHOP_ID = createShop(MERCHANT_ID, SHOP_NAME);

        String FIRST_ITEM_ID = addItemToShop(SHOP_ID, FIRST_ITEM_URL, FIRST_ITEM_ALT_TEXT, FIRST_ITEM_NAME, FIRST_ITEM_COST, FIRST_ITEM_INV);
        String EDIT_ITEM_ID = addItemToShop(SHOP_ID, EDIT_ITEM_URL, EDIT_ITEM_ALT_TEXT, EDIT_ITEM_NAME, EDIT_ITEM_COST, EDIT_ITEM_INV);

        String temp = editItemInShop(SHOP_ID, EDIT_ITEM_ID, MERCHANT_ID, EDIT_ITEM_URL, EDIT_ITEM_ALT_TEXT, EDIT_ITEM_NAME, EDITED_ITEM_COST, EDITED_ITEM_INV);

        setDoneFlag(false);
        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();

        testDbInstance.getReference("test/store/" + SHOP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> curStoreData = (Map<String, Object>) ((Map<String, Object>) dataSnapshot.getValue()).get("item");

                Map<String, Object> firstItemData = (Map<String, Object>) curStoreData.get(FIRST_ITEM_ID);
                Long invVal = (Long) firstItemData.get("inventory");
                int inv = invVal != null ? invVal.intValue() : null;
                toVerify.put("Incorrect inventory for first item", new Object[]{inv, Integer.parseInt(FIRST_ITEM_INV)});
                String cost = (String) firstItemData.get("cost");
                toVerify.put("Incorrect cost for first item", new Object[]{cost, FIRST_ITEM_COST});


                Map<String, Object> secondItemData = (Map<String, Object>) curStoreData.get(EDIT_ITEM_ID);
                invVal = (Long) secondItemData.get("inventory");
                int inventory = invVal != null ? invVal.intValue() : null;
                toVerify.put("Incorrect inventory for edited item", new Object[]{inventory, Integer.parseInt(EDITED_ITEM_INV)});
                String secondInvVal = (String) secondItemData.get("cost");
                toVerify.put("Incorrect cost for edited item", new Object[]{secondInvVal, EDITED_ITEM_COST});

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

            assertEquals(reason, val1, val2);
        }

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);
    }

    @Test
    public void itRemovesAnItemFromAShop() {
        final String SHOP_NAME = "itRemovesAItemFromAShopID";

        String MERCHANT_ID = createMerchant("aUsername", "aPassword", "anEmail", "aPhoneNumber");
        String SHOP_ID = createShop(MERCHANT_ID, SHOP_NAME);

        System.out.println(SHOP_ID);

        String FIRST_ITEM_ID = addItemToShop(SHOP_ID, "aURL1", "someAltText1", "anItemName1", "1.00", "1");
        String REMOVE_ITEM_ID = addItemToShop(SHOP_ID, "aURL2", "someAltText2", "anItemName2", "2.00", "2");
        String THIRD_ITEM_ID = addItemToShop(SHOP_ID, "aURL3", "someAltText3", "anItemName3", "3.00", "3");

        String temp = removeItemFromShop(SHOP_ID, REMOVE_ITEM_ID);

        setDoneFlag(false);
        ArrayList<String> toVerify = new ArrayList<String>();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/item/").addListenerForSingleValueEvent(new ValueEventListener() {
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
            System.out.println(String.format("Verifying %s does not equal %s", id, REMOVE_ITEM_ID));

            assertNotEquals("Item was not deleted", id, REMOVE_ITEM_ID);
        }

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);
    }

    @Test
    public void itAddsAnItemToAShop() {
        final String NAME = "itAddsAItemToAShop";
        final String URL = "http://google.ca";
        final String ALT_TEXT = "Google";
        final String ITEM_NAME = "anItemName";
        final String COST = "5.99";
        final String INVENTORY = "20";
        final int INT_INV = 20;

        String MERCHANT_ID = createMerchant("aUsername", "aPassword", "anEmail", "aPhoneNumber");
        String SHOP_ID = createShop(MERCHANT_ID, NAME);

        String ITEM_ID = addItemToShop(SHOP_ID, URL, ALT_TEXT, ITEM_NAME, COST, INVENTORY);

        setDoneFlag(false);
        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/item/" + ITEM_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> currItemData = (Map<String, Object>) dataSnapshot.getValue();

                String itemName = (String) currItemData.get("name");
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

            assertEquals(reason, val1, val2);
        }

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);
    }

    @Test
    public void itRemovesATagFromAShop() {
        final String NAME = "itRemovesATagFromAShop";
        final String FIRST_TAG_NAME = "firstTag";
        final String REMOVE_TAG_NAME = "removeTag";
        final String THIRD_TAG_NAME = "thirdTag";

        String MERCHANT_ID = createMerchant("aUsername", "aPassword", "anEmail", "aPhoneNumber");
        String SHOP_ID = createShop(MERCHANT_ID, NAME);

        String FIRST_TAG_ID = addTagToShop(SHOP_ID, FIRST_TAG_NAME);
        String REMOVE_TAG_ID = addTagToShop(SHOP_ID, REMOVE_TAG_NAME);
        String THIRD_TAG_ID = addTagToShop(SHOP_ID, THIRD_TAG_NAME);

        String temp = removeTagFromShop(SHOP_ID, REMOVE_TAG_ID);

        setDoneFlag(false);
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

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);
    }

    @Test
    public void itAddsATagToAShop() {
        final String NAME = "itAddsATagToAShop";
        final String TAG_NAME = "aTagName";

        String MERCHANT_ID = createMerchant("aUsername", "aPassword", "a@a.com", "1234567890");

        String SHOP_ID = createShop(MERCHANT_ID, NAME);

        String TAG_ID = addTagToShop(SHOP_ID, TAG_NAME);

        setDoneFlag(false);
        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();

        testDbInstance.getReference("test/store/" + SHOP_ID + "/tag/" + TAG_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toVerify.put("Tag was not added to the store.", new Object[]{(String) dataSnapshot.getValue(), TAG_NAME});
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

            assertEquals(reason, val1, val2);
        }

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);
    }

    @Test
    public void itCreatesAShop() {
        final String USERNAME = "aUsername";

        String MERCHANT_ID = createMerchant(USERNAME, "aPassword", "a@a.com", "1234567890");

        final String SHOP_NAME = "itCreatesAShop";

        String SHOP_ID = createShop(MERCHANT_ID, SHOP_NAME);

        setDoneFlag(false);
        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();

        testDbInstance.getReference("test/store/" + SHOP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String shopName = (String) ((Map<String, Object>) dataSnapshot.getValue()).get("shopName");
                toVerify.put("Could not find the store in the DB.", new Object[]{shopName, SHOP_NAME});
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

            assertEquals(reason, val1, val2);
        }

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);

        ArrayList<String> toVerify2 = new ArrayList<String>();
        setDoneFlag(false);

        testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/shops").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> temp = (Map<String, Object>) dataSnapshot.getValue();

                Set<String> keys = temp.keySet();

                for (String key : keys) {
                    toVerify2.add((String) temp.get(key));
                }
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

        assertEquals(toVerify2.contains(SHOP_ID), true);

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);
    }

    @Test
    public void itDeletesAShop() {
        final String FIRST_SHOP_NAME = "firstShopID";
        final String DELETE_SHOP_NAME = "deleteShopID";
        final String THIRD_SHOP_NAME = "thirdShopID";

        final String USERNAME = "aUsername";

        String MERCHANT_ID = createMerchant(USERNAME, "aPassword", "a@a.com", "1234567890");

        String FIRST_SHOP_ID = createShop(MERCHANT_ID, FIRST_SHOP_NAME);

        String DELETE_SHOP_ID = createShop(MERCHANT_ID, DELETE_SHOP_NAME);

        String THIRD_SHOP_ID = createShop(MERCHANT_ID, THIRD_SHOP_NAME);

        deleteShop(DELETE_SHOP_ID, MERCHANT_ID);

        setDoneFlag(false);
        ArrayList<String> shopNames = new ArrayList<String>();

        testDbInstance.getReference("test/store/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                    String currName = (String) ((Map<String, Object>) storeData.getValue()).get("shopName");
                    shopNames.add(currName);
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

        assertEquals(shopNames.contains(DELETE_SHOP_NAME), false);

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);

        setDoneFlag(false);
        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();

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

            assertEquals(reason, val1, val2);
        }

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);
    }

    @Test
    public void itChangesAShopName() {
        final String ORIGINAL_NAME = "itChangesAShopName";
        final String NEW_NAME = "aBetterName";

        final String USERNAME = "aUsername";
        String password = "aPassword";
        String email = "a@a.com";
        String phoneNum = "1234567890";

        String MERCHANT_ID = createMerchant(USERNAME, password, email, phoneNum);

        String SHOP_ID = createShop(MERCHANT_ID, ORIGINAL_NAME);

        String NAME_CHANGE_ID = changeShopName(SHOP_ID, NEW_NAME);

        setDoneFlag(false);
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

            assertEquals(reason, val1, val2);
        }

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);
    }

    @Test
    public void itCreatesAMerchant() {
        final String USERNAME = "aUsername";
        String password = "aPassword";
        String email = "a@a.com";
        String phoneNum = "1234567890";

        String MERCHANT_ID = createMerchant(USERNAME, password, email, phoneNum);

        setDoneFlag(false);
        Map<String, Object[]> toVerify = new HashMap<String, Object[]>();

        testDbInstance.getReference("test/users/merchants/" + MERCHANT_ID + "/userName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = (String) dataSnapshot.getValue();
                toVerify.put("Merchant was not added.", new Object[]{USERNAME, userName});

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

            assertEquals(reason, val1, val2);
        }

        assertEquals("OnDataChange method did not run", getResult(), PASS_FLAG);
    }

    @Test
    public void itLoginsCorrectly() {
        final String USERNAME = "aUsername";
        final String PASSWORD = "aPassword";
        String email = "a@a.com";
        String phoneNum = "1234567890";
        String MERCHANT_ID = createMerchant(USERNAME, PASSWORD, email, phoneNum);

        final String SHOP_NAME = "itCreatesAShop";
        String SHOP_ID = createShop(MERCHANT_ID, SHOP_NAME);

        String MERCHANT_LOGIN_ID = merchantLogin(USERNAME, PASSWORD);

        assertNotEquals("Could not find the store in the DB.", MERCHANT_LOGIN_ID, functionCaller.getCallErrorMsg());
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
        DONE_FLAG = false;
        DELAY_COUNTER = 0;
    }

    public static void setResult(String res) {
        result = res;
    }

    public static String getResult() {
        return result;
    }
}
