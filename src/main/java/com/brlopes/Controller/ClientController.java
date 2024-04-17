package com.brlopes.Controller;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brlopes.Model.Client;
import com.brlopes.Service.ClientService;
import com.brlopes.Service.exceptions.ErrorResponse;
import com.brlopes.Service.exceptions.NoClientIdException;
import com.brlopes.dto.ClientDTO;

@RestController
@RequestMapping("/client")
public class ClientController implements Serializable {
    
    @Autowired
    private ClientService clientService;
    
    @GetMapping("/") // Read - NORMAL CLIENT ROLE
    public ResponseEntity<?> findAll() {
        Iterable<Client> list = clientService.findAll();
        
        List<ClientDTO> listDTO = StreamSupport.stream(list.spliterator(), false)
        .map(client -> new ClientDTO(client.getName(), client.getAge()))
        .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }
    
    @PostMapping("/add")  // Create - ADMIN ROLE
    public ResponseEntity<?> addClient(@RequestBody Client client){
        clientService.insert(client);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}") // Read - ADMIN ROLE
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Client client = clientService.findById(id);
            return ResponseEntity.ok().body(client);
        } catch (NoClientIdException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Client not found in DataBase. Register first.");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }
    
    
    @DeleteMapping("/{id}") // Delete (by ID) - ADMIN ROLE
    public ResponseEntity<ErrorResponse> deletebyId(@PathVariable Long id) {
        clientService.deletebyId(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}") // Update (by ID) - ADMIN ROLE
    public ResponseEntity<ErrorResponse> updateById(@PathVariable Long id, @RequestBody Client client){
        try {
            client = clientService.updateById(id, client);
            return ResponseEntity.ok().build();
        } catch (com.brlopes.Service.exceptions.NoClientIdException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Client not found in DataBase. Register first.");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }
}