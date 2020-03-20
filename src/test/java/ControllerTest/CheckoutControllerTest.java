package ShopifyProj;

import ShopifyProj.Controller.CheckoutController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckoutControllerTest {
    @Autowired
    private CheckoutController checkoutController;

    @Test
    public void checkoutContextLoads() throws Exception {
        assertThat(checkoutController).isNotNull();
    }
}
