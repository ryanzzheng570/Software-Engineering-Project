package ShopifyProj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ControllerTest {

    @Autowired
    private ShopController shopCont;

    @Autowired
    private ThymeController thymeCont;

    @Test
    public void shopContextLoads() throws Exception {
        assertThat(shopCont).isNotNull();
    }

    @Test
    public void thymeContextLoads() throws Exception {
        assertThat(thymeCont).isNotNull();
    }
}