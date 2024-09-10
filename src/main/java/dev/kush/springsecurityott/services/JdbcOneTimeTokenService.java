package dev.kush.springsecurityott.services;

import dev.kush.springsecurityott.models.OneTimeTokens;
import dev.kush.springsecurityott.models.OneTimeTokensImpl;
import dev.kush.springsecurityott.repos.OneTimeTokensRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.ott.GenerateOneTimeTokenRequest;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.authentication.ott.OneTimeTokenAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;
import java.util.function.Predicate;

@Component
public class JdbcOneTimeTokenService implements OneTimeTokenService {

    private final OneTimeTokensRepository oneTimeTokensRepository;
    private final UserDetailsManager userDetailsManager;

    public JdbcOneTimeTokenService(OneTimeTokensRepository oneTimeTokensRepository, UserDetailsManager userDetailsManager) {
        this.oneTimeTokensRepository = oneTimeTokensRepository;
        this.userDetailsManager = userDetailsManager;
    }

    @Override
    @NonNull
    public OneTimeToken generate(GenerateOneTimeTokenRequest request) {

//        if (!userDetailsManager.userExists(request.getUsername())) {
//            return new OneTimeTokensImpl();
//        }

        OneTimeTokens ott = new OneTimeTokens(request.getUsername(), UUID.randomUUID().toString());
        oneTimeTokensRepository.save(ott);
        return new OneTimeTokensImpl(ott);
    }

    @Override
    public OneTimeToken consume(OneTimeTokenAuthenticationToken authenticationToken) {
        OneTimeTokens ott = oneTimeTokensRepository.findByToken(authenticationToken.getTokenValue()).orElse(null);
        if (ott == null || isExpired(ott) /** || ott.isUsed == 1 **/) {
            return null;
        }
        // here you have to delete it or apply some logic
        // like flag in db for isUsed so that it only used once
        oneTimeTokensRepository.delete(ott);
        // oneTimeTokensRepository.setIsUsed(1)
        return new OneTimeTokensImpl(ott);
    }

    boolean isExpired(OneTimeTokens ott) {
        return ott.getExpiresAt().isBefore(Instant.now());
    }
}
