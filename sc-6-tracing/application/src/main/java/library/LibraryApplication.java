package library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

import lombok.RequiredArgsConstructor;

@EnableFeignClients
// Required to disable Spring Security basic authenticaiton
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
@RequiredArgsConstructor
public class LibraryApplication {
    public static final String UI_CONFIG_NAME = "uiConfig";

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }
}
