package ShopifyProj;

import ShopifyProj.Repository.ShopRepository;
import ShopifyProj.Repository.ItemRepository;

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
public class ShopAndSearchIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopRepository shopRepo;

    @Autowired
    private ItemRepository itemRepo;

    @Test
    public void searchShopNoMatch() throws Exception {
        String searchField = "testShop";
        String requestStr = String.format("/search?searchField=%s", searchField);

        this.mockMvc.perform(post(requestStr)).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void searchShopByName() throws Exception {
        //Create a shop
        String name = "TEST_SHOP";
        String tag1 = "TAG_1";

        String requestStr = String.format("/addShop?shopName=%s&tag=%s", name, tag1);

        this.mockMvc.perform(post(requestStr));

        //Search for the shop by name
        String searchField = "TEST_SHOP";
        String requestStr2 = String.format("/search?searchField=%s", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));

    }

    @Test
    public void searchShopByPartialName() throws Exception {
        //Create a shop
        String name = "TEST_SHOP123456";
        String tag1 = "TAG_1";

        String requestStr = String.format("/addShop?shopName=%s&tag=%s", name, tag1);

        this.mockMvc.perform(post(requestStr));

        //Search for the shop by partial name
        String searchField = "234";
        String requestStr2 = String.format("/search?searchField=%s", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void searchShopByTag() throws Exception {
        //Create a shop
        String name = "TEST_SHOP";
        String tag1 = "TAG_1";

        String requestStr = String.format("/addShop?shopName=%s&tag=%s", name, tag1);

        this.mockMvc.perform(post(requestStr));

        //Search for the shop by name
        String searchField = "TAG_1";
        String requestStr2 = String.format("/search?searchField=%s", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void searchShopByPartialTag() throws Exception {
        //Create a shop
        String name = "TEST_SHOP";
        String tag1 = "TAG_123456";

        String requestStr = String.format("/addShop?shopName=%s&tag=%s", name, tag1);

        this.mockMvc.perform(post(requestStr));

        //Search for the shop by name
        String searchField = "234";
        String requestStr2 = String.format("/search?searchField=%s", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void searchShopUppercaseName() throws Exception {
        //Create a shop
        String name = "BookShop";
        String tag1 = "test";
        String requestStr = String.format("/addShop?shopName=%s&tag=%s", name, tag1);


        this.mockMvc.perform(post(requestStr));

        //Shop 1 contains name with book and tag with book
        String searchField = "bookshop";
        String requestStr2 = String.format("/search?searchField=%s", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

    @Test
    public void searchShopUppercaseTag() throws Exception {
        //Create a shop
        String name = "BookShop";
        String tag1 = "TEST";
        String requestStr = String.format("/addShop?shopName=%s&tag=%s", name, tag1);


        this.mockMvc.perform(post(requestStr));

        //Shop 1 contains name with book and tag with book
        String searchField = "test";
        String requestStr2 = String.format("/search?searchField=%s", searchField);

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
        String requestStr = String.format("/addShop?shopName=%s&tag=%s", name, tag1);


        this.mockMvc.perform(post(requestStr));

        //Shop 1 contains name with book and tag with book
        String searchField = "BoOk";
        String requestStr2 = String.format("/search?searchField=%s", searchField);

        this.mockMvc.perform(post(requestStr2)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)))
                .andExpect(content().string(containsString(tag1)));
    }

}