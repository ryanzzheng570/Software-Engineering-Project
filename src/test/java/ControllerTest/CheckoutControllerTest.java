package ShopifyProj;

import ShopifyProj.Controller.CheckoutController;
import ShopifyProj.Controller.FirebaseController;
import ShopifyProj.Model.Customer;
import org.junit.AfterClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckoutControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private CheckoutController checkoutController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void checkoutContextLoads() throws Exception {
        assertThat(checkoutController).isNotNull();
    }

    @Test
    public void goToCheckoutPageNotLoggedIn() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/goToCart",
                String.class)).contains("Login");
    }

    @Test
    public void goToCheckoutPageLoggedIn() throws Exception {
        FirebaseController.setCurrUser(new Customer());
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/goToCart",
                String.class)).contains("Shopping Cart");
    }

    @AfterEach
    public void cleanup() {
        FirebaseController.setCurrUser(null);
    }
}
