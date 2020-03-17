//package ShopifyProj;
//
//import ShopifyProj.Repository.ItemRepository;
//import ShopifyProj.Repository.ShopRepository;
//import ShopifyProj.Repository.TagRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.not;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ShopAndDiffViewIntegrationTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private TagRepository tagRepo;
//
//    @Autowired
//    private ShopRepository shopRepo;
//
//    @Autowired
//    private ItemRepository itemRepo;
//
//    @Test
//    public void addBasicShop() throws Exception {
//        String name = "TEST_SHOP";
//
//        String requestStr = String.format("/addShop?shopName=%s", name);
//
//        this.mockMvc.perform(post(requestStr));
//        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(name)));
//    }
//
//    @Test
//    public void testChangeShopName() throws Exception {
//        String name = "TEST_SHOP";
//        String newName = "NEW_NAME";
//
//        String requestStr = String.format("/addShop?shopName=%s", name);
//        this.mockMvc.perform(post(requestStr));
//
//        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(name)));
//
//        int shopId = shopRepo.findByShopName(name).getId();
//
//        String changeNameQuery = String.format("/changeShopName?shopId=%d&shopName=%s", shopId, newName);
//        this.mockMvc.perform(post(changeNameQuery));
//
//        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(not(containsString(name))))
//                .andExpect(content().string(containsString(newName)));
//    }
//
//    @Test
//    public void testDeleteShop() throws Exception {
//        String name = "TEST_SHOP_DEL";
//        String name2 = "TEST_SHOP_2_DEL";
//
//        String requestStr = String.format("/addShop?shopName=%s", name);
//        this.mockMvc.perform(post(requestStr));
//
//        requestStr = String.format("/addShop?shopName=%s", name2);
//        this.mockMvc.perform(post(requestStr));
//
//        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(name)))
//                .andExpect(content().string(containsString(name2)));
//
//        int shopId = shopRepo.findByShopName(name).getId();
//
//        String delShopQuery = String.format("/deleteShop?shopId=%d", shopId);
//        this.mockMvc.perform(post(delShopQuery));
//
//        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(not(containsString(name))))
//                .andExpect(content().string(containsString(name2)));
//    }
//
//    @Test
//    public void addShopOneTags() throws Exception {
//        String name = "TEST_SHOP_ONE_TAG";
//        String tag1 = "TAG_1";
//
//        String requestStr = String.format("/addShop?shopName=%s", name);
//        this.mockMvc.perform(post(requestStr));
//
//        int shopId = shopRepo.findByShopName(name).getId();
//
//        String addTagQuery = String.format("/addTag?shopId=%d&tagName=%s", shopId, tag1);
//        this.mockMvc.perform(post(addTagQuery));
//
//        this.mockMvc.perform(post(requestStr));
//        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(name)))
//                .andExpect(content().string(containsString(tag1)));
//    }
//
//    @Test
//    public void addShopWithTwoTags() throws Exception {
//        String name = "TEST_SHOP_TWO_TAGS";
//        String tag1 = "TAG_1";
//        String tag2 = "TAG_2";
//
//        String requestStr = String.format("/addShop?shopName=%s", name);
//        this.mockMvc.perform(post(requestStr));
//
//        int shopId = shopRepo.findByShopName(name).getId();
//
//        String addTagQuery = String.format("/addTag?shopId=%d&tagName=%s", shopId, tag1);
//        this.mockMvc.perform(post(addTagQuery));
//
//        addTagQuery = String.format("/addTag?shopId=%d&tagName=%s", shopId, tag2);
//        this.mockMvc.perform(post(addTagQuery));
//
//        this.mockMvc.perform(post(requestStr));
//        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(name)))
//                .andExpect(content().string(containsString(tag1)))
//                .andExpect(content().string(containsString(tag2)));
//    }
//
//    @Test
//    public void testRemoveTag() throws Exception {
//        String name = "TEST_SHOP_REM_TAG";
//        String tag1 = "TAG_1_REM_TAG";
//        String tag2 = "TAG_2";
//
//        String requestStr = String.format("/addShop?shopName=%s", name);
//        this.mockMvc.perform(post(requestStr));
//
//        int shopId = shopRepo.findByShopName(name).getId();
//
//        String addTagQuery = String.format("/addTag?shopId=%d&tagName=%s", shopId, tag1);
//        this.mockMvc.perform(post(addTagQuery));
//
//        addTagQuery = String.format("/addTag?shopId=%d&tagName=%s", shopId, tag2);
//        this.mockMvc.perform(post(addTagQuery));
//
//        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(name)))
//                .andExpect(content().string(containsString(tag1)))
//                .andExpect(content().string(containsString(tag2)));
//
//        int tagId = tagRepo.findByTagName(tag1).getId();
//
//        String remTagQuery = String.format("/removeTag?shopId=%d&tagId=%d", shopId, tagId);
//        this.mockMvc.perform(post(remTagQuery));
//
//        this.mockMvc.perform(get("/goToMerchantMenuPage")).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string(containsString(name)))
//                .andExpect(content().string(not(containsString(tag1))))
//                .andExpect(content().string(containsString(tag2)));
//    }
//
//    @Test
//    public void testAddItemToStore() throws Exception {
//        String name = "TEST_SHOP_ADD_ITEM";
//
//        String itemName = "ITEM_NAME";
//        String itemUrl = "";
//        String itemAltText = "";
//        String itemCost = "45";
//        int itemInventory = 56;
//
//        String requestStr = String.format("/addShop?shopName=%s", name);
//        this.mockMvc.perform(post(requestStr));
//
//        int shopId = shopRepo.findByShopName(name).getId();
//
//        String addItemQuery = String.format("/addItem?shopId=%d&url=%s&altText=%s&itemName=%s&cost=%s&inventory=%d",
//                shopId,
//                itemUrl,
//                itemAltText,
//                itemName,
//                itemCost,
//                itemInventory);
//        this.mockMvc.perform(post(addItemQuery));
//
////        this.mockMvc.perform(get("/goToShopCustomerView?shopId=" + shopId)).andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(content().string((containsString(itemName))))
////                .andExpect(content().string((containsString(itemCost))))
////                .andExpect(content().string((containsString(Integer.toString(itemInventory)))));
//
//        this.mockMvc.perform(get("/goToEditShopPage?shopId=" + shopId)).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string((containsString(itemName))))
//                .andExpect(content().string((containsString(itemCost))))
//                .andExpect(content().string((containsString(Integer.toString(itemInventory)))));
//    }
//
//    @Test
//    public void testRemoveItem() throws Exception {
//        String name = "TEST_SHOP_REMOVE_ITEM";
//
//        String item1Name = "ITEM_NAME";
//        String item1Url = "";
//        String item1AltText = "";
//        String item1Cost = "45";
//        int item1Inventory = 56;
//
//        String item2Name = "ITEM_2_NAME";
//        String item2Url = "";
//        String item2AltText = "";
//        String item2Cost = "698";
//        int item2Inventory = 9867;
//
//        String requestStr = String.format("/addShop?shopName=%s", name);
//        this.mockMvc.perform(post(requestStr));
//
//        int shopId = shopRepo.findByShopName(name).getId();
//
//        String addItemQuery = String.format("/addItem?shopId=%d&url=%s&altText=%s&itemName=%s&cost=%s&inventory=%d",
//                shopId,
//                item1Url,
//                item1AltText,
//                item1Name,
//                item1Cost,
//                item1Inventory);
//        this.mockMvc.perform(post(addItemQuery));
//
//        addItemQuery = String.format("/addItem?shopId=%d&url=%s&altText=%s&itemName=%s&cost=%s&inventory=%d",
//                shopId,
//                item2Url,
//                item2AltText,
//                item2Name,
//                item2Cost,
//                item2Inventory);
//        this.mockMvc.perform(post(addItemQuery));
//
////        this.mockMvc.perform(get("/goToShopCustomerView?shopId=" + shopId)).andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(content().string((containsString(item1Name))))
////                .andExpect(content().string((containsString(item1Cost))))
////                .andExpect(content().string((containsString(Integer.toString(item1Inventory)))))
////                .andExpect(content().string((containsString(item2Name))))
////                .andExpect(content().string((containsString(item2Cost))))
////                .andExpect(content().string((containsString(Integer.toString(item2Inventory)))));
//
//        this.mockMvc.perform(get("/goToEditShopPage?shopId=" + shopId)).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string((containsString(item1Name))))
//                .andExpect(content().string((containsString(item1Cost))))
//                .andExpect(content().string((containsString(Integer.toString(item1Inventory)))))
//                .andExpect(content().string((containsString(item2Name))))
//                .andExpect(content().string((containsString(item2Cost))))
//                .andExpect(content().string((containsString(Integer.toString(item2Inventory)))));
//
//        int itemId = itemRepo.findByItemName(item1Name).getId();
//
//        String remItemQuery = String.format("/removeItem?shopId=%d&itemId=%d",
//                shopId,
//                itemId);
//        this.mockMvc.perform(post(remItemQuery));
//
////        this.mockMvc.perform(get("/goToShopCustomerView?shopId=" + shopId)).andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(content().string((not(containsString(item1Name)))))
////                .andExpect(content().string((not(containsString(item1Cost)))))
////                .andExpect(content().string((not(containsString(Integer.toString(item1Inventory))))))
////                .andExpect(content().string((containsString(item2Name))))
////                .andExpect(content().string((containsString(item2Cost))))
////                .andExpect(content().string((containsString(Integer.toString(item2Inventory)))));
//
//        this.mockMvc.perform(get("/goToEditShopPage?shopId=" + shopId)).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string((not(containsString(item1Name)))))
//                .andExpect(content().string((not(containsString(item1Cost)))))
//                .andExpect(content().string((not(containsString(Integer.toString(item1Inventory))))))
//                .andExpect(content().string((containsString(item2Name))))
//                .andExpect(content().string((containsString(item2Cost))))
//                .andExpect(content().string((containsString(Integer.toString(item2Inventory)))));
//    }
//
//    @Test
//    public void goToNewShop() throws Exception {
//        String shopName = "goToNewShop";
//
//        String addShopQuery = String.format("/addShop?shopName=%s&tag=%s", shopName, "tagOne");
//
//        this.mockMvc.perform(post(addShopQuery));
//
////        this.mockMvc.perform(get("/goToShopCustomerView?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(content().string((containsString("Welcome to the online store for " + shopName))));
//
//        this.mockMvc.perform(get("/goToEditShopPage?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().string((containsString("Shop"))));
//    }
//
//    @Test
//    public void goToShopWithTagsAndItems() throws Exception {
//        String shopName = "goToShopWithTags";
//        String tagOne = "Tag One";
//        String tagTwo = "Tag Two";
//
//        String itemName = "ITEM";
//        String cost = "10";
//        String inventory = "30";
//
//        String addShopQuery = String.format("/addShop?shopName=%s", shopName);
//        this.mockMvc.perform(post(addShopQuery));
//
//        int shopId = shopRepo.findByShopName(shopName).getId();
//
//        String addTagQuery = String.format("/addTag?shopId=%d&tagName=%s", shopId, tagOne);
//        this.mockMvc.perform(post(addTagQuery));
//
//        addTagQuery = String.format("/addTag?shopId=%d&tagName=%s", shopId, tagTwo);
//        this.mockMvc.perform(post(addTagQuery));
//
//        String addItemQuery = String.format("/addItem?shopId=%s&url=%s&altText=%s&itemName=%s&cost=%s&inventory=%s",
//                shopRepo.findByShopName(shopName).getId(),
//                "",
//                "",
//                itemName,
//                cost,
//                inventory);
//        this.mockMvc.perform(post(addItemQuery));
//
////        this.mockMvc.perform(get("/goToShopCustomerView?shopId=" + shopId)).andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(content().string((containsString(shopName))))
////                .andExpect(content().string((containsString(tagOne))))
////                .andExpect(content().string((containsString(tagTwo))))
////                .andExpect(content().string((containsString(itemName))))
////                .andExpect(content().string((containsString(cost))))
////                .andExpect(content().string((containsString(inventory))));
//
//        this.mockMvc.perform(get("/goToEditShopPage?shopId=" + shopId)).andDo(print())
//                .andExpect(status().isOk())
//                //.andExpect(content().string((containsString(shopName))))
//                .andExpect(content().string((containsString(tagOne))))
//                .andExpect(content().string((containsString(tagTwo))))
//                .andExpect(content().string((containsString(itemName))))
//                .andExpect(content().string((containsString(cost))))
//                .andExpect(content().string((containsString(inventory))));
//    }
//
////    @Test
////    public void goToShopWithoutItems() throws Exception {
////        String shopName = "goToShopWithoutItems";
////        String noItemsMessage = "Sorry, the merchant did not add items to their store yet!";
////
////        this.mockMvc.perform(post("/addShop?shopName=" + shopName + "&tag=tagOne"));
////
////        this.mockMvc.perform(get("/goToShopCustomerView?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(content().string((containsString(noItemsMessage))));
////    }
//}