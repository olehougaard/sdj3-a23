package dk.via.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BranchServer {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BranchServer.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run();
    }
}
