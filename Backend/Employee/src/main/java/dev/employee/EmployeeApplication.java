package dev.employee;

import dev.common.constant.PackageConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {PackageConstant.COMMON_PACKAGE, PackageConstant.EMPLOYEE_PACKAGE})
@EnableDiscoveryClient
public class EmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

}
