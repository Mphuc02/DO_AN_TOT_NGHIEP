package dev.authentication.repository;

import dev.authentication.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUserNameOrEmailOrNumberPhone(String userName, String email, String numberPhone);
    Account findByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}