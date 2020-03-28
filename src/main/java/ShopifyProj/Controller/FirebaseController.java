package ShopifyProj.Controller;

import ShopifyProj.Model.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class FirebaseController {
    private static FirebaseController inst = null;
    private static FirebaseDatabase dbInst = null;
    public static final String TEST_MODE = "TEST";
    public static final String PRODUCTION_MODE = "PRODUCTION";
    private static ArrayList<Shop> dbShops;
    private static boolean isCustomer = false;
    private static User currUser = null;

    private static final AtomicLong counter = new AtomicLong();

    private FirebaseController() {
        FileInputStream serviceAccount =
                null;
        try {
            serviceAccount = new FileInputStream("src/main/resources/firebase-auth-key.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://engineeringlabproject.firebaseio.com")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FirebaseApp.initializeApp(options);
        dbInst = FirebaseDatabase.getInstance();
    }

    private static ArrayList<Shop> initializeDbInfo() {
        ArrayList<Shop> tempDbShops = new ArrayList<Shop>();

        CountDownLatch wait = new CountDownLatch(1);
        FirebaseController.getInstance().getReference("store").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot storeData : dataSnapshot.getChildren()) {
                        String shopId = storeData.getKey();

                        Map<String, Object> mapData = (Map<String, Object>) storeData.getValue();

                        String shopName = (String) mapData.get("shopName");
                        Shop shopToAdd = new Shop(shopName, Optional.empty());
                        shopToAdd.setId(shopId);

                        Map<String, Object> tagData = (Map<String, Object>) mapData.get("tag");
                        if (tagData != null) {
                            for (String tagId : tagData.keySet()) {
                                String tagName = (String) tagData.get(tagId);

                                Tag tagToAdd = new Tag(tagName);
                                tagToAdd.setId(tagId);

                                shopToAdd.addTag(tagToAdd);
                            }
                        }

                        Map<String, Object> itemData = (Map<String, Object>) mapData.get("item");
                        if (itemData != null) {
                            for (String itemId : itemData.keySet()) {
                                Map<String, Object> currItemData = (Map<String, Object>) itemData.get(itemId);

                                String itemName = (String) currItemData.get("name");

                                Double cost = null;
                                try {
                                    Long costVal = (Long) currItemData.get("cost");
                                    cost = costVal != null ? costVal.doubleValue() : null;
                                } catch (Exception e) {
                                    try {
                                        cost = (Double) currItemData.get("cost");
                                    } catch (Exception f) {
                                        String costVal = (String) currItemData.get("cost");
                                        cost = Double.parseDouble(costVal);
                                    }
                                }

                                Long invVal = (Long) currItemData.get("inventory");
                                int inventory = invVal != null ? invVal.intValue() : null;
                                String url = (String) currItemData.get("url");
                                String altText = (String) currItemData.get("altText");

                                ArrayList<Image> itemImages = new ArrayList<Image>();
                                if (!url.equals("")) {
                                    Image imgToAdd = new Image(url, altText);
                                    itemImages.add(imgToAdd);
                                }

                                Item itemToAdd = new Item(itemName, itemImages, cost, inventory);
                                itemToAdd.setId(itemId);
                                itemToAdd.setStoreID(shopId);
                                itemToAdd.setStoreName(shopName);
                                shopToAdd.addItem(itemToAdd);
                            }
                        }

                        tempDbShops.add(shopToAdd);

                    }
                }
                wait.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                wait.countDown();
            }

        });
        try {
            wait.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (tempDbShops);
    }

    public static FirebaseDatabase getInstance() {
        if (inst == null) {
            inst = new FirebaseController();
        }

        return (dbInst);
    }

    public static void loadDbInfo(boolean reloadIfLoaded) {
        if ((dbShops == null) || (reloadIfLoaded)) {
            dbShops = initializeDbInfo();
        }
    }

    public static ArrayList<Shop> getDbShops() {
        return dbShops;
    }

    public static Shop getShopWithId(String shopId) throws Exception {
        Shop checkShop = null;
        for (Shop shop : dbShops) {
            if (shop.getId().equals(shopId)) {
                checkShop = shop;
                break;
            }
        }
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %s", shopId));
            throw new Exception();
        }
        return checkShop;
    }

    public static ArrayList<Item> getItemsFromStoreByIds(String aShopID, String[] itemIDs) {
        Shop toParse = null;
        try {
            toParse = FirebaseController.getShopWithId(aShopID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Item> items = new ArrayList<Item>();

        List idList = Arrays.asList(itemIDs);
        for (Item item : toParse.getItems()) {
            if (idList.contains(item.getId())) {
                items.add(item);
            }
        }

        return items;
    }

    public static Shop findByShopName(String name) throws Exception {
        for (Shop shop : dbShops) {
            if (shop.getShopName().equals(name)) {
                return shop;
            }
        }

        throw new Exception();
    }

    public static Tag findByTagName(String shopId, String tagName) throws Exception {
        try {
            Shop shop = getShopWithId(shopId);

            for (Tag tag : shop.getTags()) {
                if (tag.getTagName().equals(tagName)) {
                    return tag;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new Exception();

    }

    public static Item findByItemName(String shopId, String itemName) throws Exception {
        try {
            Shop shop = getShopWithId(shopId);

            for (Item item : shop.getItems()) {
                if (item.getItemName().equals(itemName)) {
                    return item;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new Exception();

    }

    public static void addShop(Shop toAdd) {
        dbShops.add(toAdd);
    }

    public static String getCounterAndIterate() {
        return Integer.toString(Math.toIntExact(counter.incrementAndGet()));
    }

    public static User getCurrUser() {
        return currUser;
    }

    public static boolean isCurrUserCustomer() {return isCustomer;}

    public static void setCurrUser(User newUser) {
        if(newUser instanceof Customer) {
            isCustomer = true;
        } else {
            isCustomer = false;
        }
        currUser = newUser;
    }

    public static List<Shop> getCurrUsersShops() {
        List<Shop> toRet = null;
        if (FirebaseController.getCurrUser() instanceof Merchant) {
            toRet = ((Merchant) FirebaseController.getCurrUser()).getShops();
        }
        return toRet;
    }

    public static Object[] getShoppingCartItems(String userID, String mode) {
        String root = "";
        ArrayList<String> items = new ArrayList<String>();
        ArrayList<String> stores = new ArrayList<String>();
        CountDownLatch wait = new CountDownLatch(1);
        if (mode == TEST_MODE) {
            root = "/test/";
        }
        FirebaseController.getInstance().getReference(root + "users/customers/" + userID + "/shoppingCart/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot storeData : dataSnapshot.getChildren()) {

                        Map<String, Object> mapData = (Map<String, Object>) storeData.getValue();

                        String itemID = (String) mapData.get("itemID");
                        items.add(itemID);
                        String storeID = (String) mapData.get("shopID");
                        stores.add(storeID);
                    }
                }
                wait.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                wait.countDown();
            }

        });
        try {
            wait.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object[]{stores, items};
    }

    public static Item getItemFromStore(String aShopID, String itemID) {
        Shop toParse = null;
        try {
            toParse = FirebaseController.getShopWithId(aShopID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Item item : toParse.getItems()) {
            if (itemID.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

}