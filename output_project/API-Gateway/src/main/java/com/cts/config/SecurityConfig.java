package com.cts.config;

import com.cts.utility.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((exchange, e) -> {
                            var response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.UNAUTHORIZED);
                            response.getHeaders().add("Content-Type", "application/json");
                            var buffer = response.bufferFactory()
                                    .wrap("{\"status\":401,\"message\":\"Unauthorized — token missing or invalid\"}".getBytes());
                            return response.writeWith(Mono.just(buffer));
                        })
                        .accessDeniedHandler((exchange, e) -> {
                            var response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.FORBIDDEN);
                            response.getHeaders().add("Content-Type", "application/json");
                            var buffer = response.bufferFactory()
                                    .wrap("{\"status\":403,\"message\":\"Forbidden — insufficient role\"}".getBytes());
                            return response.writeWith(Mono.just(buffer));
                        })
                )

                .authorizeExchange(auth -> auth

                        .pathMatchers("/auth/**").permitAll()

                        .pathMatchers("/api/admin/auth/**").permitAll()
                        .pathMatchers("/api/admin/getUserDetails").permitAll()
                        .pathMatchers("/api/admin/addUsers").permitAll()
                        .pathMatchers("/api/admin/shipment/**").permitAll()
                        .pathMatchers("/api/admin/internal/**").permitAll()
                        .pathMatchers("/audit/internal").permitAll()

                        .pathMatchers("/api/admin/users/**").hasRole("ADMIN")

                        .pathMatchers("/api/admin/**").authenticated()
                        .pathMatchers("/api/shipment/**").authenticated()
                        .pathMatchers("/api/delivery/**").authenticated()
                        .pathMatchers("/api/reporting/**").authenticated()
                        .pathMatchers("/vehicles/**").authenticated()
                        .pathMatchers("/routes/**").authenticated()
                        .pathMatchers("/schedules/**").authenticated()
                        .pathMatchers("/drivers/**").authenticated()
                        .pathMatchers("/assignments/**").authenticated()
                        .pathMatchers("/trips/**").authenticated()
                        .pathMatchers("/inspections/**").authenticated()
                        .pathMatchers("/maintenance/**").authenticated()
                        .pathMatchers("/audit/**").authenticated()   // /audit/logs needs token

                        .anyExchange().authenticated()
                )

                .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
