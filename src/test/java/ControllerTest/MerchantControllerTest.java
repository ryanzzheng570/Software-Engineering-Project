package ShopifyProj;

import ShopifyProj.Controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MerchantControllerTest {
    @Autowired
    private MerchantController merchCont;

    @Test
    public void merchContextLoads() throws Exception {
        assertThat(merchCont).isNotNull();
    }
}