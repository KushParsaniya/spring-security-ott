package dev.kush.springsecurityott.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Map;

@Controller
@ResponseBody
public class SecuredController {

    private final HandlerMapping resourceHandlerMapping;

    public SecuredController(@Qualifier("resourceHandlerMapping") HandlerMapping resourceHandlerMapping) {
        this.resourceHandlerMapping = resourceHandlerMapping;
    }

    @GetMapping("/user")
    public Map<String, String> user(Authentication authentication, HttpServletResponse response, HttpSession session) throws IOException {
//        response.setHeader("X-USER-ROLE","USER");
//        Cookie cookie = new Cookie("X-USER-ROLE","USER");
//        cookie.setAttribute("JSESSIONID",session.getId());
//        response.addCookie(cookie);
//        response.sendRedirect("http://localhost:5173/");
        return Map.of("message", "Hello user %s, welcome!".formatted(authentication.getName()));
    }

    @GetMapping("/admin")
    public Map<String, String> admin(Authentication authentication) {
        return Map.of("message", "Hello admin %s, welcome!".formatted(authentication.getName()));
    }

    @GetMapping("/sent")
    public String sent(HttpServletResponse response) {
        return "<h1>We have send magic link to your mail</h1>";
    }

    @GetMapping("/any")
    public String any() {
        return "Any called";
    }
}
