package dev.kush.springsecurityott.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ott.GeneratedOneTimeTokenHandler;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class WebSecurityConfig {

    private final OneTimeTokenService oneTimeTokenService;
    private final GeneratedOneTimeTokenHandler generatedOneTimeTokenHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final JwtDecoder jwtDecoder;

    public WebSecurityConfig(OneTimeTokenService oneTimeTokenService, GeneratedOneTimeTokenHandler generatedOneTimeTokenHandler, AuthenticationSuccessHandler authenticationSuccessHandler, JwtDecoder jwtDecoder) {
        this.oneTimeTokenService = oneTimeTokenService;
        this.generatedOneTimeTokenHandler = generatedOneTimeTokenHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.jwtDecoder = jwtDecoder;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(
                        req -> req.requestMatchers("/user").access(OAuth2AuthorizationManagers.hasAnyScope("ROLE_ADMIN","ROLE_USER"))
                                .requestMatchers("/admin").access(OAuth2AuthorizationManagers.hasScope("ROLE_ADMIN"))
                                .requestMatchers("/sent").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
//                .oauth2ResourceServer(oauth -> oauth.jwt(
//                        jwtConfigurer -> jwtConfigurer
//                                .decoder(jwtDecoder)
//                                .jwtAuthenticationConverter(source -> {
//                                    String scopes = source.getClaimAsString("scope");
//
//                                    final Set<GrantedAuthority> authorities = Arrays.stream(scopes.split(" "))
//                                            .map(SimpleGrantedAuthority::new)
//                                            .collect(Collectors.toSet());
//
//                                    return new BearerTokenAuthentication(
//                                            new DefaultOAuth2AuthenticatedPrincipal(Map.of("provider","self"), authorities),
//                                            new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, source.getTokenValue(), source.getIssuedAt(), source.getExpiresAt()),
//                                            authorities
//                                    );
//                                })
//                ))
                .exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .oneTimeTokenLogin(
                        configurer -> configurer
                                .generatedOneTimeTokenHandler(generatedOneTimeTokenHandler)
                                .oneTimeTokenService(oneTimeTokenService)
                                .authenticationSuccessHandler(authenticationSuccessHandler)
                );
        return httpSecurity.build();
    }
}
