package ShopifyProj.Controller;

import ShopifyProj.Model.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.ui.Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class FirebaseController {
    private static FirebaseController inst = null;
    private static FirebaseDatabase dbInst = null;

    private static ArrayList<Shop> currShops = initializeDbInfo();

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

                        String shopName = (String) mapData.get("name");
                        Shop shopToAdd = new Shop(shopName, Optional.empty());
                        shopToAdd.setId(shopId);

                        Map<String, Object> tagData = (Map<String, Object>) mapData.get("tag");
                        for (String tagId : tagData.keySet()) {
                            String tagName = (String) tagData.get(tagId);

                            Tag tagToAdd = new Tag(tagName);
                            tagToAdd.setId(tagId);

                            shopToAdd.addTag(tagToAdd);
                        }

                        Map<String, Object> itemData = (Map<String, Object>) mapData.get("item");
                        for (String itemId : itemData.keySet()) {
                            Map<String, Object> currItemData = (Map<String, Object>) itemData.get(itemId);

                            String itemName = (String) currItemData.get("name");
                            String cost = Double.toString((Double) currItemData.get("cost"));

                            Long invVal = (Long) currItemData.get("inventory");
                            int inventory = invVal != null ? invVal.intValue() : null;
                            String url = (String) currItemData.get("url");
                            String altText = (String) currItemData.get("altText");

                            Image imgToAdd = new Image(url, altText);
                            ArrayList<Image> itemImages = new ArrayList<Image>();
                            itemImages.add(imgToAdd);

                            Item itemToAdd = new Item(itemName, itemImages, cost, inventory);
                            itemToAdd.setId(itemId);
                            shopToAdd.addItem(itemToAdd);
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

        return(dbShops);
    }

    public static FirebaseDatabase getInstance() {
        if (inst == null) {
            inst = new FirebaseController();
        }

        return (dbInst);
    }

    public static ArrayList<Shop> getCurrShops() {
        return currShops;
    }

    public static Shop getShopWithId(String shopId) {
        Shop checkShop = null;
        for (Shop shop : currShops) {
            if (shop.getId().equals(shopId)) {
                checkShop = shop;
                break;
            }
        }
        if (checkShop == null) {
            System.out.println(String.format("No shop found with ID %d", shopId));
        }
        return checkShop;
    }

    public static TempShop getShopFromID(String aShopID) {
        final TempShop[] shop = {null};
        CountDownLatch wait = new CountDownLatch(1);
        FirebaseController.getInstance().getReference("store/" + aShopID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    shop[0] = dataSnapshot.getValue(TempShop.class);
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
        return shop[0];
    }

    public static ArrayList<TempItem> getItemsFromStoreByIds(String aShopID, String[] itemIDs) {
        ArrayList<TempItem> items = new ArrayList<TempItem>();
        CountDownLatch wait = new CountDownLatch(1);
        FirebaseController.getInstance().getReference("store/" + aShopID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TempShop shop = dataSnapshot.getValue(TempShop.class);
                    for (int i = 0; i< itemIDs.length; i++) {
                        TempItem tempItem = shop.getItemByKey(itemIDs[i]);
                        if (tempItem != null) {
                            items.add(tempItem);
                        }
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
        return items;
    }

}