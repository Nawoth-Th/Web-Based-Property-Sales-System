package com.university.propertysales.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails seller = User.builder()
                .username("seller")
                .password(passwordEncoder().encode("seller123"))
                .roles("SELLER")
                .build();

        UserDetails buyer = User.builder()
                .username("buyer")
                .password(passwordEncoder().encode("buyer123"))
                .roles("BUYER")
                .build();

        UserDetails agent = User.builder()
                .username("agent")
                .password(passwordEncoder().encode("agent123"))
                .roles("AGENT")
                .build();

        return new InMemoryUserDetailsManager(admin, seller, buyer, agent);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        // Public static resources and pages
                        .requestMatchers("/", "/index.html", "/login.html", "/register.html",
                                "/property.html", "/dashboard.html", "/profile.html",
                                "/offers.html", "/agreements.html").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()

                        // Public API endpoints - must be before more specific rules
                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers("/api/properties", "/api/properties/**").permitAll()

                        .requestMatchers("/api/**").permitAll()

                        .anyRequest().permitAll()
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }
}
