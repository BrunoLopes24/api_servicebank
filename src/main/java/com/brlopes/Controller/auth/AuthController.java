package com.brlopes.Controller.auth;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brlopes.Model.Login;
import com.brlopes.Repository.LoginRepo;
import com.brlopes.Service.authService.TokenService;
import com.brlopes.dto.AuthenticationDTO;
import com.brlopes.dto.LoginResponseDTO;
import com.brlopes.dto.registerDTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    
    @Autowired
    private LoginRepo loginRepo;
    
    @Autowired
    TokenService tokenService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var usernamepassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
            var auth = this.authManager.authenticate(usernamepassword);
            
            var token = tokenService.generateToken((Login)auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (UsernameNotFoundException e) {
            // Return 401 Unauthorized when user is not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (BadCredentialsException e) {
            // Retorne 401 Unauthorized quando as credenciais são inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid registerDTO data) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<registerDTO>> violations = validator.validate(data);
        if (!violations.isEmpty()) {
            return ResponseEntity.badRequest().body("Validation errors occur");
        }
        // Check for duplicate username
        if (this.loginRepo.findByUsername(data.login()) != null) {
            return ResponseEntity.badRequest().build();
        }
        
        // Encrypt the password
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        
        // Create new Login instance
        Login newClient = new Login(data.login(), encryptedPassword, data.role());
        
        // Save new client login
        this.loginRepo.save(newClient);
        
        // Return OK status code for successful registration
        return ResponseEntity.ok().build();
    }
}