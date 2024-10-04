package dev.websocket;

import dev.common.constant.PackageConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import static dev.common.constant.PackageConstant.COMMON_PACKAGE;

@EnableDiscoveryClient
@EnableFeignClients(COMMON_PACKAGE)
@SpringBootApplication(scanBasePackages = {PackageConstant.COMMON_PACKAGE,
        PackageConstant.WEBSOCKET_PACKAGE})
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }

}
