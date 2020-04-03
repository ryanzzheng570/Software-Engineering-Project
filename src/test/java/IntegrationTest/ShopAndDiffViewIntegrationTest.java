package ShopifyProj;

import ShopifyProj.Controller.FirebaseController;
import ShopifyProj.Model.Merchant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShopAndDiffViewIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addBasicShop() throws Exception {
        String name = "TEST_SHOP";

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(requestStr));

        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(merchantUsername)));
    }

    @Test
    public void testChangeShopName() throws Exception {
        String name = "TEST_SHOP";
        String newName = "NEW_NAME";

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(requestStr));

        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(merchantUsername)));

        String shopId = FirebaseController.findByShopName(name).getId();

        String changeNameQuery = String.format("/changeShopName?shopId=%s&shopName=%s", shopId, newName);
        this.mockMvc.perform(post(changeNameQuery));

        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString(name))))
                .andExpect(content().string(containsString(newName)));
    }

    @Test
    public void testDeleteShop() throws Exception {
        String name = "TEST_SHOP_DEL";
        String name2 = "TEST_SHOP_2_DEL";

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(requestStr));

        requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name2, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(requestStr));

        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(name2)))
                .andExpect(content().string(containsString(merchantUsername)));

        String shopId = FirebaseController.findByShopName(name).getId();

        String delShopQuery = String.format("/deleteShop?shopId=%s", shopId);
        this.mockMvc.perform(post(delShopQuery));

        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString(name))))
                .andExpect(content().string(containsString(name2)));
    }

    @Test
    public void addShopOneTags() throws Exception {
        String name = "TEST_SHOP_ONE_TAG";
        String tag1 = "TAG_1";

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag1, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        this.mockMvc.perform(post(requestStr));
        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(merchantUsername)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void addShopWithTwoTags() throws Exception {
        String name = "TEST_SHOP_TWO_TAGS";
        String tag1 = "TAG_1";
        String tag2 = "TAG_2";

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag1, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag2, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        this.mockMvc.perform(post(requestStr));
        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(merchantUsername)))
                .andExpect(content().string(containsString(tag1)))
                .andExpect(content().string(containsString(tag2)));
    }

    @Test
    public void testRemoveTag() throws Exception {
        String name = "TEST_SHOP_REM_TAG";
        String tag1 = "TAG_1_REM_TAG";
        String tag2 = "TAG_2";

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag1, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag2, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(merchantUsername)))
                .andExpect(content().string(containsString(tag1)))
                .andExpect(content().string(containsString(tag2)));

        String tagId = FirebaseController.findByTagName(shopId, tag1).getId();

        String remTagQuery = String.format("/removeTag?shopId=%s&tagId=%s", shopId, tagId);
        this.mockMvc.perform(post(remTagQuery));

        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(not(containsString(tag1))))
                .andExpect(content().string(containsString(tag2)));
    }

    @Test
    public void testAddItemToStore() throws Exception {
        String name = "TEST_SHOP_ADD_ITEM";

        String itemName = "ITEM_NAME";
        String itemUrl = "";
        String itemAltText = "";
        String itemCost = "45";
        int itemInventory = 56;

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addItemQuery = String.format("/addItem?shopId=%s&url=%s&altText=%s&itemName=%s&cost=%s&inventory=%d&setId=%s&testMode=true",
                shopId,
                itemUrl,
                itemAltText,
                itemName,
                itemCost,
                itemInventory,
                FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addItemQuery));

        this.mockMvc.perform(get("/goToShopCustomerView?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(itemName))))
                .andExpect(content().string((containsString(itemCost))))
                .andExpect(content().string((containsString(Integer.toString(itemInventory)))));

        this.mockMvc.perform(get("/goToEditShopPage?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(itemName))))
                .andExpect(content().string((containsString(itemCost))))
                .andExpect(content().string((containsString(Integer.toString(itemInventory)))));
    }

    @Test
    public void testRemoveItem() throws Exception {
        String name = "TEST_SHOP_REMOVE_ITEM";

        String item1Name = "ITEM_NAME";
        String item1Url = "";
        String item1AltText = "";
        String item1Cost = "45";
        int item1Inventory = 56;

        String item2Name = "ITEM_2_NAME";
        String item2Url = "";
        String item2AltText = "";
        String item2Cost = "698";
        int item2Inventory = 9867;

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addItemQuery = String.format("/addItem?shopId=%s&url=%s&altText=%s&itemName=%s&cost=%s&inventory=%d&setId=%s",
                shopId,
                item1Url,
                item1AltText,
                item1Name,
                item1Cost,
                item1Inventory,
                FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addItemQuery));

        addItemQuery = String.format("/addItem?shopId=%s&url=%s&altText=%s&itemName=%s&cost=%s&inventory=%d&setId=%s",
                shopId,
                item2Url,
                item2AltText,
                item2Name,
                item2Cost,
                item2Inventory,
                FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addItemQuery));

        this.mockMvc.perform(get("/goToShopCustomerView?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(item1Name))))
                .andExpect(content().string((containsString(item1Cost))))
                .andExpect(content().string((containsString(Integer.toString(item1Inventory)))))
                .andExpect(content().string((containsString(item2Name))))
                .andExpect(content().string((containsString(item2Cost))))
                .andExpect(content().string((containsString(Integer.toString(item2Inventory)))));

        this.mockMvc.perform(get("/goToEditShopPage?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(item1Name))))
                .andExpect(content().string((containsString(item1Cost))))
                .andExpect(content().string((containsString(Integer.toString(item1Inventory)))))
                .andExpect(content().string((containsString(item2Name))))
                .andExpect(content().string((containsString(item2Cost))))
                .andExpect(content().string((containsString(Integer.toString(item2Inventory)))));

        String itemId = FirebaseController.findByItemName(shopId, item1Name).getId();

        String remItemQuery = String.format("/removeItem?shopId=%s&itemId=%s",
                shopId,
                itemId);
        this.mockMvc.perform(post(remItemQuery));

        this.mockMvc.perform(get("/goToShopCustomerView?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((not(containsString(item1Name)))))
                .andExpect(content().string((not(containsString(item1Cost)))))
                .andExpect(content().string((not(containsString(Integer.toString(item1Inventory))))))
                .andExpect(content().string((containsString(item2Name))))
                .andExpect(content().string((containsString(item2Cost))))
                .andExpect(content().string((containsString(Integer.toString(item2Inventory)))));

        this.mockMvc.perform(get("/goToEditShopPage?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((not(containsString(item1Name)))))
                .andExpect(content().string((not(containsString(item1Cost)))))
                .andExpect(content().string((not(containsString(Integer.toString(item1Inventory))))))
                .andExpect(content().string((containsString(item2Name))))
                .andExpect(content().string((containsString(item2Cost))))
                .andExpect(content().string((containsString(Integer.toString(item2Inventory)))));
    }

    @Test
    public void goToNewShop() throws Exception {
        String shopName = "goToNewShop";

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        String addShopQuery = String.format("/addShop?shopName=%s&setId=%s&testMode=true", shopName, FirebaseController.getCounterAndIterate());

        this.mockMvc.perform(post(addShopQuery));

        String shopId = FirebaseController.findByShopName(shopName).getId();

        this.mockMvc.perform(get("/goToShopCustomerView?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString("Welcome to the online store for " + shopName))));

        this.mockMvc.perform(get("/goToEditShopPage?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString("Shop"))));
    }

    @Test
    public void goToShopWithTagsAndItems() throws Exception {
        String shopName = "goToShopWithTags";
        String tagOne = "Tag One";
        String tagTwo = "Tag Two";

        String itemName = "ITEM";
        String cost = "10";
        String inventory = "30";

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        String addShopQuery = String.format("/addShop?shopName=%s&setId=%s&testMode=true", shopName, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addShopQuery));

        String shopId = FirebaseController.findByShopName(shopName).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tagOne, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tagTwo, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        String addItemQuery = String.format("/addItem?shopId=%s&url=%s&altText=%s&itemName=%s&cost=%s&inventory=%s&setId=%s",
                shopId,
                "",
                "",
                itemName,
                cost,
                inventory,
                FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addItemQuery));

        this.mockMvc.perform(get("/goToShopCustomerView?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(shopName))))
                .andExpect(content().string((containsString(tagOne))))
                .andExpect(content().string((containsString(tagTwo))))
                .andExpect(content().string((containsString(itemName))))
                .andExpect(content().string((containsString(cost))))
                .andExpect(content().string((containsString(inventory))));

        this.mockMvc.perform(get("/goToEditShopPage?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(shopName))))
                .andExpect(content().string((containsString(tagOne))))
                .andExpect(content().string((containsString(tagTwo))))
                .andExpect(content().string((containsString(itemName))))
                .andExpect(content().string((containsString(cost))))
                .andExpect(content().string((containsString(inventory))));
    }

    @Test
    public void goToShopWithoutItems() throws Exception {
        String shopName = "goToShopWithoutItems";
        String noItemsMessage = "Sorry, the merchant did not add items to their store yet!";

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        String query = String.format("/addShop?shopName=%s&setId=%s&testMode=true", shopName, FirebaseController.getCounterAndIterate());

        this.mockMvc.perform(post(query));

        String shopId = FirebaseController.findByShopName(shopName).getId();

        this.mockMvc.perform(get("/goToShopCustomerView?testMode=true&shopId=" + shopId)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(noItemsMessage))));
    }

    @Test
    public void testMerchantMenuNavNoLogin() throws Exception {
        FirebaseController.setCurrUser(null);

        // Should bring you to merchant login page when not logged in
        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login As A Merchant")));
    }

    @Test
    public void testMerchantMenuNavWithLogin() throws Exception {
        FirebaseController.setCurrUser(null);

        // Should bring you to merchant login page when not logged in
        this.mockMvc.perform(get("/goToMerchantMenuPage"));

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        // Should bring you to merchant menu page when logged in
        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Merchant Menu Page")))
                .andExpect(content().string(containsString(merchantUsername)));
    }

    @Test
    public void testMerchantMenuNavWithLogout() throws Exception {
        FirebaseController.setCurrUser(null);

        // Should bring you to merchant login page when not logged in
        this.mockMvc.perform(get("/goToMerchantMenuPage"));

        String merchantUsername = "USERNAME";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), merchantUsername);
        this.mockMvc.perform(post(requestStr));

        // Should bring you to merchant menu page when logged in
        this.mockMvc.perform(get("/goToMerchantMenuPage"));

        this.mockMvc.perform(get("/logout")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Home Page")));

        // Should bring you to merchant login page when not logged in
        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login As A Merchant")));
    }

    @Test
    public void testShoppingCartNavWithoutLogin()  throws Exception{
        FirebaseController.setCurrUser(null);

        // Should bring you to customer login page when not logged in
        this.mockMvc.perform(get("/goToCart")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login As A Customer")));
    }

    @Test
    public void testShoppingCartNavWithLogin() throws Exception {
        FirebaseController.setCurrUser(null);

        // Should bring you to customer login page when not logged in
        this.mockMvc.perform(get("/goToCart"));

        String customerUsername = "USERNAME";

        String requestStr = String.format("/loginAsCustomer?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), customerUsername);
        this.mockMvc.perform(post(requestStr));

        // Should bring you to shopping cart page when logged in
        this.mockMvc.perform(get("/goToCart")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Shopping Cart")));
    }

    @Test
    public void testShoppingCartMenuNavWithLogout() throws Exception{
        FirebaseController.setCurrUser(null);

        // Should bring you to customer login page when not logged in
        this.mockMvc.perform(get("/goToCart"));

        String customerUsername = "USERNAME";

        String requestStr = String.format("/loginAsCustomer?id=%s&userName=%s&testMode=true", FirebaseController.getCounterAndIterate(), customerUsername);
        this.mockMvc.perform(post(requestStr));

        // Should bring you to shopping cart page when logged in
        this.mockMvc.perform(get("/goToCart"));

        this.mockMvc.perform(get("/logout")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Home Page")));

        // Should bring you to customer login page when not logged in
        this.mockMvc.perform(get("/goToCart")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Login As A Customer")));
    }

    @Test
    public void testDiffMerchantsHaveDiffShops() throws Exception {
        String shopName1 = "SHOP_ONE";
        String shopName2 = "SHOP_TWO";

        String merchId1 = FirebaseController.getCounterAndIterate();
        String merchId2 = FirebaseController.getCounterAndIterate();

        String shopId1 = FirebaseController.getCounterAndIterate();
        String shopId2 = FirebaseController.getCounterAndIterate();

        String merchantUsername = "USERNAME";
        String merchantUsername2 = "USER_NAME_2";

        String requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", merchId1, merchantUsername);
        this.mockMvc.perform(post(requestStr));

        String addShopQuery = String.format("/addShop?shopName=%s&setId=%s&testMode=true", shopName1, shopId1);
        this.mockMvc.perform(post(addShopQuery));

        // Should bring you to merchant menu page when logged in
        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Merchant Menu Page")))
                .andExpect(content().string(containsString(merchantUsername)))
                .andExpect(content().string(containsString(shopName1)))
                .andExpect(content().string(not(containsString(shopName2))));

        requestStr = String.format("/loginAsMerchant?id=%s&userName=%s&testMode=true", merchId2, merchantUsername2);
        this.mockMvc.perform(post(requestStr));

        addShopQuery = String.format("/addShop?shopName=%s&setId=%s&testMode=true", shopName2, shopId2);
        this.mockMvc.perform(post(addShopQuery));

        // Should bring you to merchant menu page when logged in
        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Merchant Menu Page")))
                .andExpect(content().string(containsString(merchantUsername2)))
                .andExpect(content().string(not(containsString(shopName1))))
                .andExpect(content().string(containsString(shopName2)));

        requestStr = String.format("/loginAsMerchant?id=%s&shops[]=%s&userName=%s&testMode=true", merchId1, shopId1, merchantUsername);
        this.mockMvc.perform(post(requestStr));

        // Should bring you to merchant menu page when logged in
        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Merchant Menu Page")))
                .andExpect(content().string(containsString(merchantUsername)))
                .andExpect(content().string(containsString(shopName1)))
                .andExpect(content().string(not(containsString(shopName2))));
    }
}