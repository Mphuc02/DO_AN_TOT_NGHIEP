package dev.hospitalinformation;

import dev.common.constant.PackageConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {PackageConstant.COMMON_PACKAGE, PackageConstant.HOSPITAL_INFORMATION_PACKAGE})
public class HospitalInformationApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalInformationApplication.class, args);
    }

}
