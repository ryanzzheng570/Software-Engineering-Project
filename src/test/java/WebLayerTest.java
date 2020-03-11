package ShopifyProj;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ShopifyProj.Repository.ItemRepository;
import ShopifyProj.Repository.ShopRepository;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShopRepository shopRepo;

    @Autowired
    private ItemRepository itemRepo;

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
                .andExpect(content().string(containsString(name)));
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
                .andExpect(content().string(containsString(tag1)));
    }
}