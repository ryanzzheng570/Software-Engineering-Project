package ShopifyProj;

import ShopifyProj.Controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ShopControllerTest {
    @Autowired
    private ShopController shopCont;

    @Test
    public void shopContextLoads() throws Exception {
        assertThat(shopCont).isNotNull();
    }
}