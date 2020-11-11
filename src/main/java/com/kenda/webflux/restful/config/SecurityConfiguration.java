package com.kenda.webflux.restful.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationManager authenticationManager;

    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() ->
                        swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() ->
                        swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))).and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers(
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html**",
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html/**",
                        "favicon.ico"
                ).permitAll()
                .anyExchange().authenticated()
                .and().build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                                .addSecuritySchemes("Token", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("Token", Arrays.asList("read", "write")));
    }

}
