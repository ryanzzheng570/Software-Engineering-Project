package ShopifyProj.Controller;

import ShopifyProj.Model.TempItem;
import ShopifyProj.Model.TempShop;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.ui.Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class FirebaseController {
    private static FirebaseController inst = null;
    private static FirebaseDatabase dbInst = null;

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

    public static FirebaseDatabase getInstance() {
        if (inst == null) {
            inst = new FirebaseController();
        }

        return (dbInst);
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
//                        System.out.println("itemID: " + itemIDs[i]);
                        TempItem tempItem = shop.getItemByKey(itemIDs[i]);
//                        System.out.println("tempItem: " + tempItem);
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
        System.out.println("array list " + items);
        return items;
    }

    public static boolean purchaseItems(String aShopID, String[] itemNames, ArrayList<Integer> quantities) {
        CountDownLatch wait = new CountDownLatch(1);
        FirebaseController.getInstance().getReference("store/" + aShopID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TempShop shop = dataSnapshot.getValue(TempShop.class);
                    for (int i = 0; i< itemNames.length; i++) {
//                        System.out.println("itemID: " + itemIDs[i]);
                        String tempItemID = shop.getItemIDByName(itemNames[i]);
                        TempItem tempItem = shop.getItemByKey(tempItemID);
                        System.out.println("tempItem: " + tempItem);
                        if (tempItem != null) {
                            int newInventoryCount = tempItem.inventory - quantities.get(i);
                            if (newInventoryCount < 0) {
                                System.out.println("Error - Not enough inventory!");
                            }
                            else {
                                tempItem.setInventory(newInventoryCount);
                                Map<String, Object> map = new HashMap<>();
                                map.put(tempItemID, tempItem);
                                String path = String.format("item/%s/", tempItemID);
                                FirebaseController.getInstance().getReference("store/" + aShopID).child(path).updateChildrenAsync(map);
                            }
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
        return true;
    }


}