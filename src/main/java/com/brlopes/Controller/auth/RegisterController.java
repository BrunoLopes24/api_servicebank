package com.brlopes.Controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brlopes.Model.Client;
import com.brlopes.Service.ClientService;

@RestController
@RequestMapping("/client")
public class RegisterController {

    @Autowired
    private ClientService clientService;
    
    @PostMapping("/register") // Create
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        client = clientService.insert(client);
        return ResponseEntity.ok().body(client);
    }
}
