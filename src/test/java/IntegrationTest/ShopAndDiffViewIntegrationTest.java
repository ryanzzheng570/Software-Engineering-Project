package ShopifyProj;

import ShopifyProj.Repository.ShopRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
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

    @Autowired
    private ShopRepository shopRepo;

    @Test
    public void addBasicShop() throws Exception {
        String name = "TEST_SHOP";

        String requestStr = String.format("/addShop?shopName=%s", name);

        this.mockMvc.perform(post(requestStr));
        this.mockMvc.perform(get("/goToAddShopPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)));
    }

    @Test
    public void addShopOneTags() throws Exception {
        String name = "TEST_SHOP";
        String tag1 = "TAG_1";

        String requestStr = String.format("/addShop?shopName=%s&tag=%s", name, tag1);

        this.mockMvc.perform(post(requestStr));
        this.mockMvc.perform(get("/goToAddShopPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void addShopWithTwoTags() throws Exception {
        String name = "TEST_SHOP";
        String tag1 = "TAG_1";
        String tag2 = "TAG_2";

        String requestStr = String.format("/addShop?shopName=%s&tag=%s&tag=%s", name, tag1, tag2);

        this.mockMvc.perform(post(requestStr));
        this.mockMvc.perform(get("/goToAddShopPage")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)))
                .andExpect(content().string(containsString(tag2)));
    }

    @Test
    public void goToNewShop() throws Exception {
        String shopName = "goToNewShop";

        String addShopQuery = String.format("/addShop?shopName=%s&tag=%s", shopName, "tagOne");

        this.mockMvc.perform(post(addShopQuery));

        this.mockMvc.perform(get("/goToShopCustomerView?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString("Welcome to the online store for " + shopName))));

        this.mockMvc.perform(get("/goToShopMerchantView?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
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

        String addShopQuery = String.format("/addShop?shopName=%s&tag=%s&tag=%s", shopName, tagOne, tagTwo);
        this.mockMvc.perform(post(addShopQuery));

        String addItemQuery = String.format("/addItem?shopId=%s&url=%s&altText=%s&itemName=%s&cost=%s&inventory=%s",
                shopRepo.findByShopName(shopName).getId(),
                "",
                "",
                itemName,
                cost,
                inventory);
        this.mockMvc.perform(post(addItemQuery));

        this.mockMvc.perform(get("/goToShopCustomerView?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(shopName))))
                .andExpect(content().string((containsString(tagOne))))
                .andExpect(content().string((containsString(tagTwo))))
                .andExpect(content().string((containsString(itemName))))
                .andExpect(content().string((containsString(cost))))
                .andExpect(content().string((containsString(inventory))));

        this.mockMvc.perform(get("/goToShopMerchantView?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
                .andExpect(status().isOk())
                //.andExpect(content().string((containsString(shopName))))
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

        this.mockMvc.perform(post("/addShop?shopName=" + shopName + "&tag=tagOne"));

        this.mockMvc.perform(get("/goToShopCustomerView?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(noItemsMessage))));
    }
}