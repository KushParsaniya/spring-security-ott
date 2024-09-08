package dev.kush.springsecurityott.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OTTAuthenticationSuccessHandle implements AuthenticationSuccessHandler {

    @Override
    // called after user click on magic link.
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("logged in as: -----------> " + authentication.getName());
        response.sendRedirect("/user");
    }
}
