package ShopifyProj;

import ShopifyProj.Controller.SignUpController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private SignUpController signUpController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void goToSignUpPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/signUp",
                String.class)).contains("Signing Up");
    }

    @Test
    public void signUpContextLoads() throws Exception {
        assertThat(signUpController).isNotNull();
    }
}
