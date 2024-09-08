package dev.kush.springsecurityott.repos;

import dev.kush.springsecurityott.models.OneTimeTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OneTimeTokensRepository extends JpaRepository<OneTimeTokens, Long> {

    @Query(value = """
        select o from OneTimeTokens o where o.token = :token
    """)
    Optional<OneTimeTokens> findByToken(String token);
}