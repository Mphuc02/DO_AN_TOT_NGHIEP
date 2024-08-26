package dev.authentication;

import dev.authentication.entity.Account;
import dev.authentication.entity.AccountRole;
import dev.authentication.model.Permission;
import dev.authentication.repository.AccountRepository;
import dev.common.constant.PackageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.sql.Date;
import java.util.Set;

@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = {PackageConstant.AUTHENTICATION_PACKAGE, PackageConstant.COMMON_PACKAGE})
public class AuthenticationApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    @Override
    public void run(String... args) throws Exception {
        if(accountRepository.existsByUserName("admin"))
            return;

        Account admin = Account.builder()
                .createdAt(new Date(new java.util.Date().getTime()))
                .email("admin@gmail.com")
                .numberPhone("0123456789")
                .userName("admin")
                .passWord(encoder.encode("admin"))
                .build();

        AccountRole adminRead = AccountRole.builder()
                .permission(Permission.ADMIN_READ).account(admin).build();
        AccountRole adminUpdate = AccountRole.builder()
                .permission(Permission.ADMIN_UPDATE).account(admin).build();
        AccountRole adminCreate = AccountRole.builder()
                .permission(Permission.ADMIN_CREATE).account(admin).build();
        AccountRole adminDelete = AccountRole.builder()
                .permission(Permission.ADMIN_DELETE).account(admin).build();
        Set<AccountRole> roles = Set.of(adminRead, adminUpdate, adminCreate, adminDelete);

        admin.setRoles(roles);
        accountRepository.save(admin);
    }
}