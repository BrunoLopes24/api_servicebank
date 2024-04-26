package com.brlopes.Controller;

import com.brlopes.Model.Client;
import com.brlopes.Model.Login;
import com.brlopes.Model.enums.LoginRoles;
import com.brlopes.Repository.LoginRepo;
import com.brlopes.Service.ClientService;
import com.brlopes.Service.exceptions.ErrorResponse;
import com.brlopes.Service.exceptions.NoClientIdException;
import com.brlopes.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/client")
public class ClientController implements Serializable {

    @Autowired
    private ClientService clientService;
    @Autowired
    private LoginRepo loginRepo;

    @GetMapping("/") // Read - NORMAL CLIENT ROLE
    public ResponseEntity<?> findAll() {
        Iterable<Client> list = clientService.findAll();

        List<ClientDTO> listDTO = StreamSupport.stream(list.spliterator(), false)
                .map(client -> new ClientDTO(client.getName(), client.getAge()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(listDTO);
    }

    @PostMapping("/add")  // Create - ADMIN ROLE
    public ResponseEntity<ErrorResponse> addClient(@RequestBody Client client){
        client.setRole(LoginRoles.CLIENT);
        Login login = new Login(client.getUsername(), client.getPassword(), client.getRole());
        loginRepo.save(login); // Guarda o login na tabela de Logins
        clientService.insert(client); // Em seguida, guarda o cliente
        if (client.getAge() < 18) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Clients under 18 years old cannot have account.");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
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


    /**
     * This method is used to check the balance of a client by their ID.
     * It first tries to find the client in the database using the provided ID.
     * If the client is found, it returns the client's balance wrapped in a ResponseEntity with an HTTP status of 200 (OK).
     * If the client is not found, it throws a NoClientIdException, which is caught and handled by returning an ErrorResponse
     * wrapped in a ResponseEntity with an HTTP status of 400 (Bad Request).
     *
     * @param id This is the ID of the client whose balance is to be checked.
     * @return ResponseEntity<?> This returns the client's balance if the client is found, otherwise an ErrorResponse.
     * @throws NoClientIdException This exception is thrown when the client with the provided ID is not found in the database.
     */
    @GetMapping("/balance/{id}")
    public ResponseEntity<?> checkBalancebyId(@PathVariable Long id) {
        try {
            Client client = clientService.findById(id);
            return ResponseEntity.ok().body(client.getBalance());
        } catch (NoClientIdException e) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, "Client not found in DataBase. Register first.");
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
    }
}