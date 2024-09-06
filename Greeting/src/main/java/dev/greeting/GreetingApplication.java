package dev.greeting;

import static dev.common.constant.PackageConstant.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(COMMON_PACKAGE)
@SpringBootApplication(scanBasePackages = {COMMON_PACKAGE, GREETING_PACKAGE},
                        exclude = UserDetailsServiceAutoConfiguration.class)
public class GreetingApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreetingApplication.class, args);
    }

}
