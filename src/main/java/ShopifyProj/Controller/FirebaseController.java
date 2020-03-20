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

    private static ArrayList<Shop> currShops = null;

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
        ArrayList<Shop> dbShops = new ArrayList<Shop>();
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
                                String cost = ((String) currItemData.get("cost"));

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
                                shopToAdd.addItem(itemToAdd);
                            }
                        }

                        dbShops.add(shopToAdd);

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

        return (dbShops);
    }

    public static FirebaseDatabase getInstance() {
        if (inst == null) {
            inst = new FirebaseController();
        }

        return (dbInst);
    }

    public static ArrayList<Shop> getCurrShops() {
        if (currShops == null) {
            currShops = initializeDbInfo();
        }
        return currShops;
    }

    public static Shop getShopWithId(String shopId) throws Exception {
        Shop checkShop = null;
        for (Shop shop : currShops) {
            if (shop.getId().equals(shopId)) {
                checkShop = shop;
                break;
            }
        }
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %d", shopId));
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
        for (Shop shop : currShops) {
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
        currShops.add(toAdd);
    }

    public static String getCounterAndIterate() {
        return Integer.toString(Math.toIntExact(counter.incrementAndGet()));
    }

}