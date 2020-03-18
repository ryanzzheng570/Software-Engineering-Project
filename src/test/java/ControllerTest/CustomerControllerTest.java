package ShopifyProj;

import ShopifyProj.Controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {
    @Autowired
    private CustomerController custCont;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void goToCustomerSignUpPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/createCustomerAccount",
                String.class)).contains("Create Customer Account");
    }

    @Test
    public void custContextLoads() throws Exception {
        assertThat(custCont).isNotNull();
    }
}