package com.sebastian.springcloud.app.gateway.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CorsSpec;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import reactor.core.publisher.Mono;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    private static final String SCOPE_WRITE = "SCOPE_write";
    private static final String SCOPE_READ = "SCOPE_read";

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                .authorizeExchange(authz -> authz
                    .pathMatchers("/authorized", "/logout")
                    .permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/items", "/api/products", "/api/users")
                    .permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/items/{id}/", "/api/products/{id}", "/api/users/{id}")
                    .hasAnyAuthority(SCOPE_WRITE, SCOPE_READ)
                    .pathMatchers(HttpMethod.PUT, "/api/products/**", "/api/items/**", "/api/users/**")
                    .hasAnyAuthority(SCOPE_WRITE)
                    .pathMatchers(HttpMethod.POST, "/api/products", "/api/items", "/api/users")
                    .hasAnyAuthority(SCOPE_WRITE)
                    .pathMatchers(HttpMethod.DELETE, "/api/products/**", "/api/items/**", "/api/users/**")
                    .hasAnyAuthority(SCOPE_WRITE)
                    .anyExchange()
                    .authenticated()
                )
                .cors(CorsSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(new Converter<Jwt, Mono<AbstractAuthenticationToken>>() {
                        
                        @Override
                        public Mono<AbstractAuthenticationToken> convert(Jwt source) {
                            Collection<String> roles = source.getClaimAsStringList("roles");
                            Collection<GrantedAuthority> authorities = roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList());
                            
                            return Mono.just(new JwtAuthenticationToken(source, authorities));
                        }
                    }
                )))
                .build();
    }

}
