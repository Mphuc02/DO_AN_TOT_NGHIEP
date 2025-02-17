package dev.payment;

import static dev.common.constant.PackageConstant.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(COMMON_PACKAGE)
@SpringBootApplication(scanBasePackages = {COMMON_PACKAGE, PAYMENT_PACKAGE})
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }

}
