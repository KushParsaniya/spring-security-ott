package dev.kush.springsecurityott.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@ResponseBody
public class SecuredController {

    @GetMapping("/user")
    public Map<String, String> user(Authentication authentication) {
        return Map.of("message", "Hello user %s, welcome!".formatted(authentication.getName()));
    }

    @GetMapping("/admin")
    public Map<String, String> admin(Authentication authentication) {
        return Map.of("message", "Hello admin %s, welcome!".formatted(authentication.getName()));
    }

    @GetMapping("/sent")
    public String sent() {
        return "<h1>We have send magic link to your mail</h1>";
    }
}
