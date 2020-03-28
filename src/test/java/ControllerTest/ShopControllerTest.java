package ShopifyProj;

import ShopifyProj.Controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShopControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ShopController shopCont;

    @Test
    public void goToMerchantSearchPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/goToShopMerchantView",
                String.class)).contains("Shop");
    }

    @Test
    public void goToCustomerShopViewPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/goToCustomerShopView",
                String.class)).contains("Shop");
    }

    @Test
    public void shopContextLoads() throws Exception {
        assertThat(shopCont).isNotNull();
    }
}