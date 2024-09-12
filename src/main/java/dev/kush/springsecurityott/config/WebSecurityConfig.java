package dev.kush.springsecurityott.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.ott.GeneratedOneTimeTokenHandler;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class WebSecurityConfig {

    private final OneTimeTokenService oneTimeTokenService;
    private final GeneratedOneTimeTokenHandler generatedOneTimeTokenHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    public WebSecurityConfig(OneTimeTokenService oneTimeTokenService, GeneratedOneTimeTokenHandler generatedOneTimeTokenHandler, AuthenticationSuccessHandler authenticationSuccessHandler, JwtDecoder jwtDecoder, CorsConfigurationSource corsConfigurationSource) {
        this.oneTimeTokenService = oneTimeTokenService;
        this.generatedOneTimeTokenHandler = generatedOneTimeTokenHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(
                        req -> req.requestMatchers("/user").access(OAuth2AuthorizationManagers.hasAnyScope("ROLE_ADMIN", "ROLE_USER"))
                                .requestMatchers("/admin").access(OAuth2AuthorizationManagers.hasScope("ROLE_ADMIN"))
                                .requestMatchers("/sent").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
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
