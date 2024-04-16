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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brlopes.Model.Client;
import com.brlopes.Service.ClientService;
import com.brlopes.Service.exceptions.ErrorResponse;
import com.brlopes.Service.exceptions.TokenExpiredException;
import com.brlopes.dto.ClientDTO;

@RestController
@RequestMapping("/client")
public class ClientController implements Serializable {
    
    @Autowired
    private ClientService clientService;
    
    @GetMapping("/") // Read
    public ResponseEntity<?> findAll(@RequestHeader("Authorization") String token) {
        try {
            Iterable<Client> list = clientService.findAll(token);
            
            List<ClientDTO> listDTO = StreamSupport.stream(list.spliterator(), false)
            .map(client -> new ClientDTO(client.getName(), client.getAge()))
            .collect(Collectors.toList());
            
            return ResponseEntity.ok().body(listDTO);
        }  catch (TokenExpiredException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token. Please Register/Login first to view all transactions");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }
    
    
    @GetMapping("/{id}") // Read 
    public ResponseEntity<?> findbyId(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            Client client = clientService.findById(token, id);
            return ResponseEntity.ok().body(client);
        } catch (TokenExpiredException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token. Please Register/Login first to check Client ID transactions");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        } catch (com.brlopes.Service.exceptions.NoClientIdException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Client not found in DataBase. Register first.");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }
    
    
    @DeleteMapping("/{id}") // Delete (by ID)
    public ResponseEntity<ErrorResponse> deletebyId(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        try {
            clientService.deletebyId(token, id);
            return ResponseEntity.noContent().build();
            
        } catch (TokenExpiredException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token. Please Register/Login first to check Client ID transactions");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }
    
    @PutMapping("update/{id}") // Update (by ID)
    public ResponseEntity<ErrorResponse> updateById(@RequestHeader("Authorization") String token,@PathVariable Long id, @RequestBody Client client){
        try {
            client = clientService.updateById(token ,id, client);
            return ResponseEntity.ok().build();
            
        } catch (TokenExpiredException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid Token. Please Register/Login first to update Client ID");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        } catch (com.brlopes.Service.exceptions.NoClientIdException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Client not found in DataBase. Register first.");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }
    
}