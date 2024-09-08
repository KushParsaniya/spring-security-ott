package dev.kush.springsecurityott.models;

import org.springframework.security.authentication.ott.OneTimeToken;

import java.time.Instant;

public class OneTimeTokensImpl implements OneTimeToken {

    private OneTimeTokens oneTimeTokens;

    public OneTimeTokensImpl(OneTimeTokens oneTimeTokens) {
        this.oneTimeTokens = oneTimeTokens;
    }

    public OneTimeTokensImpl() {
    }

    @Override
    public String getTokenValue() {
        return oneTimeTokens.getToken();
    }

    @Override
    public String getUsername() {
        return oneTimeTokens.getUsername();
    }

    @Override
    public Instant getExpiresAt() {
        return oneTimeTokens.getExpiresAt();
    }
}
