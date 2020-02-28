package AddressBookProj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Launcher {
    public static void main(String[] args) {
        SpringApplicationBuilder b = new SpringApplicationBuilder(Launcher.class);
        b.headless(false);
        ConfigurableApplicationContext c = b.run(args);
    }
}