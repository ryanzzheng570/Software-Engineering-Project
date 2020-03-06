package ShopifyProj;

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
public class WebApplicationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShopRepository shopRepo;

    @Test
    public void goToNewShop() throws Exception {
        String shopName = "goToNewShop";

        this.mockMvc.perform(post("/addShop?shopName=" + shopName + "&tag=tagOne"));

        this.mockMvc.perform(get("/goToShop?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString("Welcome to the online store for " + shopName))));

        this.mockMvc.perform(get("/YourShopPage?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString("Shop"))));
    }

    @Test
    public void goToShopWithTagsAndItems() throws Exception {
        String shopName = "goToShopWithTags";
        String tagOne = "Tag One";
        String tagTwo = "Tag Two";

        this.mockMvc.perform(post("/addShop?shopName=" + shopName + "&tag=" + tagOne + "&tag=" + tagTwo));
        //this.mockMvc.perform(post("/addItem?shopId=" + shopName + "&tag=" + tagOne + "&tag=" + tagTwo));

        this.mockMvc.perform(get("/goToShop?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(tagOne))))
                .andExpect(content().string((containsString(tagTwo))));
    }

    @Test
    public void goToShopWithoutItems() throws Exception {
        String shopName = "goToShopWithoutItems";
        String noItemsMessage = "Sorry, the merchant did not add items to their store yet!";

        this.mockMvc.perform(post("/addShop?shopName=" + shopName + "&tag=tagOne"));

        this.mockMvc.perform(get("/goToShop?shopId=" + shopRepo.findByShopName(shopName).getId())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string((containsString(noItemsMessage))));
    }
}