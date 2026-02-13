import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations={"classpath:config.xml"})
public class DemoApp {
    public static void main(String[] args) {

        SpringApplication.run(DemoApp.class, args);

    }
}