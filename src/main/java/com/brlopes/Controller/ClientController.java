package com.brlopes.Controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brlopes.Model.Client;
import com.brlopes.Service.ClientService;
import com.brlopes.dto.ClientDTO;

@RestController
@RequestMapping("/client")
public class ClientController {
    
    @Autowired
    private ClientService clientService;
    
    @GetMapping("/") // Read
    public ResponseEntity<List<ClientDTO>> findAll() {
        Iterable<Client> list = clientService.findAll();
        List<ClientDTO> listDTO = StreamSupport.stream(list.spliterator(), false)
        .map(client -> new ClientDTO(client.getName(), client.getAge()))
        .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }
    
    @GetMapping("/{id}") // Read 
    public ResponseEntity<Client> findbyId(@PathVariable Long id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok().body(client);
    }
    
    @DeleteMapping("/{id}") // Delete (by ID)
    public ResponseEntity<Void> deletebyId(@PathVariable Long id) {
        clientService.deletebyId(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("update/{id}") // Update (by ID)
    public ResponseEntity<Client> updateById(@PathVariable Long id, @RequestBody Client client){
        client = clientService.updateById(id, client);
        return ResponseEntity.ok().body(client);
    }
    
}