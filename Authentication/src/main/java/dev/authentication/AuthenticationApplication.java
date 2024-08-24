package dev.authentication;

import dev.common.constant.PackageConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {PackageConstant.AUTHENTICATION_PACKAGE, PackageConstant.COMMON_PACKAGE})
public class AuthenticationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }
}