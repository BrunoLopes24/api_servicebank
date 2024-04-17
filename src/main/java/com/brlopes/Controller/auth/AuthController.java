package com.brlopes.Controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

import jakarta.validation.Valid;

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
        var usernamepassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authManager.authenticate(usernamepassword);
        
        var token = tokenService.generateToken((Login)auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid registerDTO data){
        if(this.loginRepo.findByUsername(data.login()) != null){
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Login newclient = new Login(data.login(), encryptedPassword, data.role());
        
        this.loginRepo.save(newclient);
        return ResponseEntity.ok().build();
    }
}