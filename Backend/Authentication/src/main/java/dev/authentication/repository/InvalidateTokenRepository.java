package dev.authentication.repository;

import dev.authentication.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface InvalidateTokenRepository extends JpaRepository<InvalidToken, UUID> {
}