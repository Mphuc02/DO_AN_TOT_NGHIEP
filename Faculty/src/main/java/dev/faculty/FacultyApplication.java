package dev.faculty;

import dev.common.constant.PackageConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = {PackageConstant.COMMON_PACKAGE, PackageConstant.FACULTY_PACKAGE})
@EnableDiscoveryClient
public class FacultyApplication {

    public static void main(String[] args) {
        SpringApplication.run(FacultyApplication.class, args);
    }

}
