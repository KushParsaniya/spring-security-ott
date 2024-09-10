package dev.kush.springsecurityott.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ott.GeneratedOneTimeTokenHandler;

@Configuration
public class WebSecurityConfig {

    private final OneTimeTokenService oneTimeTokenService;
    private final GeneratedOneTimeTokenHandler generatedOneTimeTokenHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    public WebSecurityConfig(OneTimeTokenService oneTimeTokenService, GeneratedOneTimeTokenHandler generatedOneTimeTokenHandler, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.oneTimeTokenService = oneTimeTokenService;
        this.generatedOneTimeTokenHandler = generatedOneTimeTokenHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(
                        req -> req.requestMatchers("/user").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/admin").hasRole("ADMIN")
                                .requestMatchers("/sent").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults()))
                .oneTimeTokenLogin(
                        configurer -> configurer
                                .generatedOneTimeTokenHandler(generatedOneTimeTokenHandler)
                                .oneTimeTokenService(oneTimeTokenService)
                                .authenticationSuccessHandler(authenticationSuccessHandler)
                );
        return httpSecurity.build();
    }
}
