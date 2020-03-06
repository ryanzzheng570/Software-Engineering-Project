package ShopifyProj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void goToHomePage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("Home Page");
    }

    @Test
    public void goToAddShopPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/goToAddShopPage",
                String.class)).contains("Add Shop Page");
    }

    @Test
    public void goToSearchPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/search",
                String.class)).contains("Search for Shops");
    }

    @Test
    public void goToMerchantSearchPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/YourShopPage",
                String.class)).contains("Shop");
    }
}