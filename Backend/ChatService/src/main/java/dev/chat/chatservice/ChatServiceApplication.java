package dev.chat.chatservice;

import dev.common.constant.PackageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@RequiredArgsConstructor
@EnableConfigurationProperties
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {PackageConstant.CHAT_PACKAGE, PackageConstant.COMMON_PACKAGE})
public class ChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatServiceApplication.class, args);
    }

}