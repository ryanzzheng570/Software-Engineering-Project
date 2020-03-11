package ShopifyProj;

import ShopifyProj.Controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SearchController searchCont;

    @Test
    public void searchContextLoads() throws Exception {
        assertThat(searchCont).isNotNull();
    }

    @Test
    public void goToSearchPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/search",
                String.class)).contains("Search for Shops");
    }
}