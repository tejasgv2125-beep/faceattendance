package com.tejas.faceattendance.config;

import com.tejas.faceattendance.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                // Disable CSRF
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // ==========================================
                        // Public Pages
                        // ==========================================

                        .requestMatchers(
                                "/",
                                "/attendance",

                                "/api/recognition/**",
                                "/api/attendance/**",

                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/models/**",
                                "/uploads/**"
                        ).permitAll()

                        // ==========================================
                        // Admin Module
                        // ==========================================

                        .requestMatchers(
                                "/dashboard",
                                "/students/**",
                                "/reports/**",
                                "/settings/**",
                                "/attendance/history/**",
                                "/attendance/export/**"
                        ).authenticated()

                        // Everything else requires login

                        .anyRequest()
                        .authenticated()

                )

                .formLogin(login -> login

                        .loginPage("/")

                        .loginProcessingUrl("/login")

                        .defaultSuccessUrl("/dashboard", true)

                        .failureUrl("/?error=true")

                        .permitAll()

                )

                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/")

                        .permitAll()

                )

                .userDetailsService(userDetailsService);

        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();

    }

}