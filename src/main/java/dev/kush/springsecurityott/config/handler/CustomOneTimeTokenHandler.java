package dev.kush.springsecurityott.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.GeneratedOneTimeTokenHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOneTimeTokenHandler implements GeneratedOneTimeTokenHandler {

    @Override
    // called when user click on send Token Button
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException, ServletException {

        // send token to main or sms
        var token = oneTimeToken.getTokenValue();
        // http :// localhost:8080/
        var url = request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort() + "/login/ott?token=" + token;
        System.out.println("You can login by clicking here: " + url);

        // redirect user to this after user click on send Token button
        response.sendRedirect("/sent");
    }
}
