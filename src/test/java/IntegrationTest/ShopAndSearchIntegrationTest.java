package ShopifyProj;

import ShopifyProj.Controller.FirebaseController;
import ShopifyProj.Model.Merchant;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShopAndSearchIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void searchShopNoMatch() throws Exception {
        String searchField = "testShop";
        String requestStr = String.format("/search?searchField=%s&testMode=true", searchField);

        this.mockMvc.perform(post(requestStr)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void searchShopByName() throws Exception {
        //Create a shop
        String name = "TEST_SHOP_BY_NAME";

        String requestStr = String.format("/addShop?shopName=%s&setId=%s", name, FirebaseController.getCounterAndIterate());
        FirebaseController.setCurrUser(new Merchant());
        this.mockMvc.perform(post(requestStr));

        //Search for the shop by name
        String searchField = "TEST_SHOP";
        String requestStr2 = String.format("/search?searchField=%s&testMode=true", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)));

    }

    @Test
    public void searchShopByPartialName() throws Exception {
        //Create a shop
        String name = "TEST_SHOP123456";
        String tag1 = "TAG_1";

        String requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        FirebaseController.setCurrUser(new Merchant());
        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag1, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        //Search for the shop by partial name
        String searchField = "234";
        String requestStr2 = String.format("/search?searchField=%s&testMode=true", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void searchShopByTag() throws Exception {
        //Create a shop
        String name = "TEST_SHOP_BY_TAG";
        String tag1 = "TAG_1";

        String requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        FirebaseController.setCurrUser(new Merchant());

        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag1, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        //Search for the shop by name
        String searchField = "TAG_1";
        String requestStr2 = String.format("/search?searchField=%s&testMode=true", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void searchShopByPartialTag() throws Exception {
        //Create a shop
        String name = "TEST_SHOP_PARTIAL_TAG";
        String tag1 = "TAG_123456";

        String requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        FirebaseController.setCurrUser(new Merchant());

        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag1, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        //Search for the shop by name
        String searchField = "234";
        String requestStr2 = String.format("/search?searchField=%s&testMode=true", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void searchShopUppercaseName() throws Exception {
        //Create a shop
        String name = "BookShop_UPPERCASE";
        String tag1 = "test";

        String requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        FirebaseController.setCurrUser(new Merchant());

        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag1, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        //Shop 1 contains name with book and tag with book
        String searchField = "bookshop";
        String requestStr2 = String.format("/search?searchField=%s&testMode=true", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void searchShopUppercaseTag() throws Exception {
        //Create a shop
        String name = "BookShop_UPPERCASE_TAG";
        String tag1 = "TEST";

        String requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        FirebaseController.setCurrUser(new Merchant());

        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag1, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        //Shop 1 contains name with book and tag with book
        String searchField = "test";
        String requestStr2 = String.format("/search?searchField=%s&testMode=true", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void searchShopUppercaseQuery() throws Exception {
        //Create a shop
        String name = "book";
        String tag1 = "test";

        String requestStr = String.format("/addShop?shopName=%s&setId=%s&testMode=true", name, FirebaseController.getCounterAndIterate());
        FirebaseController.setCurrUser(new Merchant());

        this.mockMvc.perform(post(requestStr));

        String shopId = FirebaseController.findByShopName(name).getId();

        String addTagQuery = String.format("/addTag?shopId=%s&tagName=%s&setId=%s", shopId, tag1, FirebaseController.getCounterAndIterate());
        this.mockMvc.perform(post(addTagQuery));

        //Shop 1 contains name with book and tag with book
        String searchField = "BoOk";
        String requestStr2 = String.format("/search?searchField=%s&testMode=true", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @AfterEach
    public void cleanup() {
        FirebaseController.setCurrUser(null);
    }

}