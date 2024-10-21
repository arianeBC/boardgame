package com.example.boardgame.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final LoggingAccessDeniedHandler accessDeniedHandler;
    private final DataSource dataSource;

    @Autowired
    public SecurityConfig(LoggingAccessDeniedHandler accessDeniedHandler, DataSource dataSource) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.dataSource = dataSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/user/**", "/secured/**").hasAnyRole("USER", "MANAGER")
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/", "/**").permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/secured")
                )
                .logout((logout) -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )
                .exceptionHandling((exceptions) -> exceptions
                        .accessDeniedHandler(accessDeniedHandler)
                );

        // Disable CSRF and frame options for H2 console
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
        jdbcUserDetailsManager.setDataSource(dataSource);
        createDefaultUsers(jdbcUserDetailsManager);
        return jdbcUserDetailsManager;
    }

    private void createDefaultUsers(JdbcUserDetailsManager userDetailsService) {
        createUserIfNotExists(userDetailsService, "bugs", "bunny", "USER");
        createUserIfNotExists(userDetailsService, "daffy", "duck", "USER", "MANAGER");
    }

    private void createUserIfNotExists(JdbcUserDetailsManager userDetailsService, String username, String password, String... roles) {
        if (!userDetailsService.userExists(username)) {
            userDetailsService.createUser(
                    User.withUsername(username)
                            .password(passwordEncoder().encode(password))
                            .roles(roles)
                            .build()
            );
        }
    }
}
