package ShopifyProj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ShopAndCartIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addItemToCart() throws Exception {
        String name = "-M3Hm_346421XzoI_T";
        String items = "-M3Hmc_TBWdO4Wbw0I";
        String requestStr = String.format("/addToCart?items=%s&store=%s", items, name);

        this.mockMvc.perform(post(requestStr)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)));
    }

    @Test
    public void addItemsToCart() throws Exception {
        String name = "-M3Hm_346421XzoI_T";
        String[] items = {"-M3Hmc_TBWdO4Wbw0I", "-M3HmgPUtnFj9qhyWR"};
        String requestStr = String.format("/addToCart?items=%s&store=%s", Arrays.toString(items), name);

        this.mockMvc.perform(post(requestStr)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(name)));
    }

    @Test
    public void checkoutItemsInCart() throws Exception {
        String name = "-M3Hm_346421XzoI_T";
        String[] itemIds = {"-M3Hmc_TBWdO4Wbw0I", "-M3HmgPUtnFj9qhyWR"};
        Integer[] quantities = {1, 1};

        String requestStr = String.format("/checkout?storeId=%s&itemIds=%s&quantities=%s", name, Arrays.toString(itemIds), Arrays.toString(quantities));
        this.mockMvc.perform(post(requestStr)).andDo(print())
                .andExpect(status().isOk());
    }
}
