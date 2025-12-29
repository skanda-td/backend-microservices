package com.example.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/orders/admin").hasRole("ADMIN")
                        .pathMatchers("/orders/**").hasAnyRole("USER", "ADMIN")
                        .pathMatchers("/graphql").hasAnyRole("USER", "ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
                );

        return http.build();
    }

    private ReactiveJwtAuthenticationConverterAdapter jwtAuthConverter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::extractAuthorities);

        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {

        Object realmAccess = jwt.getClaims().get("realm_access");
        if (realmAccess == null) {
            return List.of();
        }

        @SuppressWarnings("unchecked")
        List<String> roles =
                (List<String>) ((Map<String, Object>) realmAccess).get("roles");

        if (roles == null) {
            return List.of();
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}