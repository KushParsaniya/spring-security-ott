package dev.kush.springsecurityott;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

@SpringBootApplication
public class SpringSecurityOttApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityOttApplication.class, args);
    }

//    @Bean
//    ApplicationRunner applicationRunner(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
//        return args -> {
//            UserDetails u1 = new User(
//                    "kush", passwordEncoder.encode("1234"), List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
//
//            UserDetails u2 = new User(
//                    "abhi", passwordEncoder.encode("1234"), List.of(new SimpleGrantedAuthority("ROLE_USER")));
//
//            userDetailsManager.createUser(u1);
//            userDetailsManager.createUser(u2);
//        };
//    }

}
