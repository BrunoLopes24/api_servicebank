package com.brlopes.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.brlopes.Repository.LoginRepo;
import com.brlopes.Service.authService.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The securityFilter class is a filter class in Spring Boot.
 * It is used to filter requests and perform authentication.
 * This class extends OncePerRequestFilter, which means it is applied once per request.
 * It uses the @Component annotation to indicate that it is a Spring component.
 */
@Component
public class securityFilter extends OncePerRequestFilter { // Filtro que acontece 1x por request.

    @Autowired
    TokenService tokenService;
    @Autowired
    LoginRepo loginRepo;

    /**
     * This method filters the request and performs authentication.
     * It recovers the token from the request, validates it, and sets the authentication in the SecurityContext.
     *
     * @param request the HttpServletRequest object.
     * @param response the HttpServletResponse object.
     * @param filterChain the FilterChain object.
     * @throws ServletException if there is an error in the servlet.
     * @throws IOException if there is an error in the input or output.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null){
            var login = tokenService.validateToken(token);
            UserDetails client = loginRepo.findByUsername(login);

            var authentication = new UsernamePasswordAuthenticationToken(client, null, client.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * This method recovers the token from the request.
     * It gets the Authorization header from the request and removes the "Bearer" prefix.
     *
     * @param request the HttpServletRequest object.
     * @return the token as a String.
     */
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        return authHeader.replace("Bearer", " ");
    }
}