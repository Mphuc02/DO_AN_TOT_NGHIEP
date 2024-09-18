package dev.medicine;

import dev.common.constant.PackageConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static dev.common.constant.PackageConstant.COMMON_PACKAGE;

@EnableFeignClients(COMMON_PACKAGE)
@SpringBootApplication(scanBasePackages = {PackageConstant.COMMON_PACKAGE,
		PackageConstant.MEDICINE_PACKAGE})
public class MedicineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicineApplication.class, args);
	}

}
