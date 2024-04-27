package com.brlopes.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The SecurityConfiguration class is a configuration class in Spring Boot.
 * It is used to configure the security settings for the application.
 * This class is typically used to set up the security filter chain, authentication manager, and password encoder.
 * It uses the @Configuration and @EnableWebSecurity annotations to indicate that it is a configuration class and to enable Spring Security's web security support.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    securityFilter securityFilter;

    /**
     * This method configures the security filter chain.
     * It sets up the security settings for different HTTP requests and adds the security filter before the UsernamePasswordAuthenticationFilter.
     * It returns a SecurityFilterChain object.
     *
     * @param httpSecurity an HttpSecurity object used to build the security filter chain.
     * @return a SecurityFilterChain object.
     * @throws Exception if there is an error in the configuration.
     */
    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
        .requestMatchers(HttpMethod.POST, "auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "auth/register").permitAll()
        .requestMatchers(HttpMethod.GET, "/client/").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/client/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/client/balance/{id}").hasRole("CLIENT")
        .requestMatchers(HttpMethod.POST, "/client/add").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/client/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/client/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/transactions/").hasRole("CLIENT")
        .requestMatchers(HttpMethod.POST, "/transactions/add").hasRole("CLIENT")
        .requestMatchers(HttpMethod.GET, "/transactions/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/transactions/{id}").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/transactions/deposit").hasRole("ADMIN")
        .anyRequest()
        .authenticated())
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    /**
     * This method returns the authentication manager.
     * It is used to authenticate the user.
     *
     * @param authenticationConfiguration an AuthenticationConfiguration object used to get the authentication manager.
     * @return an AuthenticationManager object.
     * @throws Exception if there is an error in the configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * This method returns the password encoder.
     * It is used to encode the user's password.
     *
     * @return a PasswordEncoder object.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}