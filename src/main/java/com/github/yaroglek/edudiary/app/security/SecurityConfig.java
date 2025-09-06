package com.github.yaroglek.edudiary.app.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/api/users/**", "/api/marks/**", "/api/schedule-days/**",
                                "/api/classes/**", "/api/subjects/**").authenticated()

                        .requestMatchers("/api/users/**", "/api/class-subjects/**", "/api/lessons/**",
                                "/api/schedule/**", "/api/classes/**",
                                "/api/subjects/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/api/marks/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/lessons").hasAnyAuthority("ROLE_ADMIN", "ROLE_TEACHER")

                        .requestMatchers("/admin/subjects/**", "/admin/classes/**",
                                "/admin/users/**", "/admin/schedule/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/student/marks/**", "/student/schedule/**").hasAuthority("ROLE_STUDENT")
                        .requestMatchers("/parent/marks/**", "/parent/schedule/**").hasAuthority("ROLE_PARENT")
                        .requestMatchers("/teacher/schedule/**", "/teacher/lesson/**",
                                "/teacher/lessons/**").hasAuthority("ROLE_TEACHER")

                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {
                })
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
