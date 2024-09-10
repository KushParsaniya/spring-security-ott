package dev.kush.springsecurityott.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;

@Component
public class OTTAuthenticationSuccessHandle implements AuthenticationSuccessHandler {

    private final JwtEncoder jwtEncoder;

    public OTTAuthenticationSuccessHandle(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    // called after user click on magic link.
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("logged in as: -----------> " + authentication.getName());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(Instant.now())
                .issuedAt(Instant.now().plusSeconds(3000))
                .claim("username", authentication.getName())
                .claim("roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .build();



        var params = JwtEncoderParameters.from(claims);

        Jwt jwt = jwtEncoder.encode(params);
        response.sendRedirect("http://localhost:5173?token=" + jwt.getTokenValue());
    }
}
