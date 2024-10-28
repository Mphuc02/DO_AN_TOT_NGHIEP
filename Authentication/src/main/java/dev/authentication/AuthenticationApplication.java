package dev.authentication;

import dev.authentication.entity.Account;
import dev.common.constant.KafkaTopicsConstrant;
import dev.common.dto.request.CommonRegisterEmployeeRequest;
import dev.common.dto.request.CreateFullNameRequest;
import dev.common.model.Permission;
import dev.authentication.repository.AccountRepository;
import dev.common.constant.PackageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@EnableConfigurationProperties
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {PackageConstant.AUTHENTICATION_PACKAGE, PackageConstant.COMMON_PACKAGE})
public class AuthenticationApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value(KafkaTopicsConstrant.CREATE_EMPLOYEE_TOPIC)
    private String createEmployeeTopic;

    private static final String ADMIN_STRING = "admin";

    @Override
    public void run(String... args){
        if(accountRepository.existsByUserName(ADMIN_STRING))
            return;

        Account admin = Account.builder()
                .id(UUID.randomUUID())
                .createdAt(new Date(new java.util.Date().getTime()))
                .userName(ADMIN_STRING)
                .email("admin@gmail.com")
                .numberPhone("0123456789")
                .passWord(encoder.encode(ADMIN_STRING))
                .build();

        List<Permission> permissions = List.of(Permission.ADMIN);

        CreateFullNameRequest request = new CreateFullNameRequest("Phuc",
                                                                "Minh",
                                                                   "Phuc");

        admin = accountRepository.save(admin);
        CommonRegisterEmployeeRequest registerRequest = CommonRegisterEmployeeRequest.builder()
                        .id(admin.getId())
                        .fullName(request)
                        .introduce("Người quản lý trang web")
                        .dateOfBirth(new Date(new java.util.Date().getTime()))
                        .permissions(permissions)
                        .build();
        kafkaTemplate.send(createEmployeeTopic, registerRequest);
    }
}