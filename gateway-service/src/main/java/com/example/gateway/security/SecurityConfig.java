package com.example.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**").permitAll()
                        .pathMatchers("/api/customers/**").hasAnyRole("CLERK", "UNDERWRITER", "ADMIN")
                        .pathMatchers("/api/policies/**").hasAnyRole("UNDERWRITER", "ADMIN")
                        .anyExchange().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails clerk = User.withDefaultPasswordEncoder()
                .username("clerk").password("clerk").roles("CLERK").build();
        UserDetails underwriter = User.withDefaultPasswordEncoder()
                .username("uw").password("uw").roles("UNDERWRITER").build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin").password("admin").roles("ADMIN").build();
        return new MapReactiveUserDetailsService(clerk, underwriter, admin);
    }
}
