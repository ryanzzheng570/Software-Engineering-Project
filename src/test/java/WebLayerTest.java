package ShopifyProj;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addBasicShop() throws Exception {
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
}