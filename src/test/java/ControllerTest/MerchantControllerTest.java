package ShopifyProj;

import ShopifyProj.Controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MerchantControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MerchantController merchCont;

    @Test
    public void merchContextLoads() throws Exception {
        assertThat(merchCont).isNotNull();
    }

    @Test
    public void goToCreateMerchantPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/addNewMerchant",
                String.class)).contains("Create Merchant Account");
    }
}