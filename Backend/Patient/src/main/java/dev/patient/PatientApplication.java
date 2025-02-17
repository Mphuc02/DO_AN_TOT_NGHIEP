package dev.patient;

import dev.common.constant.PackageConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static dev.common.constant.PackageConstant.COMMON_PACKAGE;

@EnableFeignClients(COMMON_PACKAGE)
@SpringBootApplication(scanBasePackages = {PackageConstant.COMMON_PACKAGE,
                                            PackageConstant.PATIENT_PACKAGE})
public class PatientApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientApplication.class, args);
    }
}