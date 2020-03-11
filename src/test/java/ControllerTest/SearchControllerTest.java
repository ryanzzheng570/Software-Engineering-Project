package ShopifyProj;

import ShopifyProj.Controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SearchControllerTest {
    @Autowired
    private SearchController searchCont;

    @Test
    public void searchContextLoads() throws Exception {
        assertThat(searchCont).isNotNull();
    }
}