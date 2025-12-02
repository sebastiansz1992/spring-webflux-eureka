package com.sebastian.springcloud.msvc.oauth.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sebastian.springcloud.msvc.oauth.models.User;

import io.micrometer.tracing.Tracer;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private WebClient webClient;

    private Tracer tracer;

    public UserService(WebClient webClient, Tracer tracer) {
        this.webClient = webClient;
        this.tracer = tracer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("Loading user by username: {}", username);

        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        try {
            User user = webClient
                    .get()
                    .uri("/users/{username}", params)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();

            logger.info("User retrieved: {}", user);
            
            List<GrantedAuthority> authorities = user.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());
            
            logger.info("User authorities: {}", authorities);
            tracer.currentSpan().tag("user.username", user.getUsername());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .accountExpired(true)
                    .accountLocked(true)
                    .credentialsExpired(true)
                    .disabled(true)
                    .authorities(authorities)
                    .build();

        } catch (Exception e) {
            logger.error("Error loading user by username: {}", username, e);
            String errorMessage = "Error loading user by username: " + username;
            logger.error(errorMessage, e);
            tracer.currentSpan().tag("error.message", errorMessage + ": " + e.getMessage());
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }

}
