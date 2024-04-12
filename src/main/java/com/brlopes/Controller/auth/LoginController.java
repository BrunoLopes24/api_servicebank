package com.brlopes.Controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brlopes.Model.Login;
import com.brlopes.Service.LoginService;

@RestController
@RequestMapping
public class LoginController {
    @Autowired
    private LoginService loginService;
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login loginRequest) {
        ResponseEntity<String> token = loginService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        return token;
    }
}