package com.ferraz.codando_a_vida_backend.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigurations {

    private final SecurityFilter securityFilter;

    public SecurityConfigurations(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        		.cors(withDefaults())
        	    .csrf(CsrfConfigurer::disable)
        	    .sessionManagement(sessionManagement ->
        	        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	    )
        	    .authorizeHttpRequests(authorizeHttpRequests ->
        	        authorizeHttpRequests
                            .requestMatchers(HttpMethod.POST, "/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/register").permitAll()
                            .requestMatchers(HttpMethod.GET, "/category").permitAll()
                            .requestMatchers(HttpMethod.GET, "/category/find-by-name/{name}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/post").permitAll()
                            .requestMatchers(HttpMethod.GET, "/post/find-by-category/{categoryName}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/post/find-by-path/{postPath}").permitAll()
                            .requestMatchers(HttpMethod.GET, "/actuator").permitAll()
                            .requestMatchers(HttpMethod.GET, "/actuator/prometheus").permitAll()
                            .anyRequest().authenticated()
        	    )
        	    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        	    .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}