package com.brlopes.api_servicebank.Service;

import com.brlopes.Model.Client;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Service.ClientService;
import com.brlopes.Service.exceptions.DatabaseException;
import com.brlopes.Service.exceptions.NoClientIdException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This class contains tests for the ClientService class.
 */
public class ClientServiceTest {

	@InjectMocks
	private ClientService clientService; // The service to be tested

	@Mock
	private ClientRepo clientRepo; // The repository used by the service

	/**
	 * This method sets up the mocks before each test.
	 */
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * This test checks if a client can be inserted successfully.
	 */
	@Test
	public void insertClientSuccessfully() {
		Client client = new Client();
		when(clientRepo.save(any(Client.class))).thenReturn(client);

		Client result = clientService.insert(client);

		assertEquals(client, result);
	}

	/**
	 * This test checks if all clients can be retrieved successfully.
	 */
	@Test
	public void findAllClientsSuccessfully() {
		clientService.findAll();

		verify(clientRepo, times(1)).findAll();
	}

	/**
	 * This test checks if a client can be found by ID successfully.
	 */
	@Test
	public void findClientByIdSuccessfully() {
		Client client = new Client();
		when(clientRepo.findById(anyLong())).thenReturn(Optional.of(client));

		Client result = clientService.findById(1L);

		assertEquals(client, result);
	}

	/**
	 * This test checks if the correct exception is thrown when trying to find a client by an ID that does not exist.
	 */
	@Test
	public void findClientByIdThrowsNoClientIdException() {
		when(clientRepo.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NoClientIdException.class, () -> clientService.findById(1L));
	}

	/**
	 * This test checks if a client can be updated by ID successfully.
	 */
	@Test
	public void updateClientByIdSuccessfully() {
		Client existingClient = new Client();
		Client newClient = new Client();
		newClient.setName("New Name");
		when(clientRepo.findById(anyLong())).thenReturn(Optional.of(existingClient));
		when(clientRepo.save(any(Client.class))).thenReturn(newClient);

		Client result = clientService.updateById(1L, newClient);

		assertEquals(newClient, result);
	}

	/**
	 * This test checks if the correct exception is thrown when trying to update a client by an ID that does not exist.
	 */
	@Test
	public void updateClientByIdThrowsNoClientIdException() {
		when(clientRepo.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(NoClientIdException.class, () -> clientService.updateById(1L, new Client()));
	}

	/**
	 * This test checks if a client can be deleted by ID successfully.
	 */
	@Test
	public void deleteClientByIdSuccessfully() {
		doNothing().when(clientRepo).deleteById(anyLong());

		assertDoesNotThrow(() -> clientService.deletebyId(1L));
	}

	/**
	 * This test checks if the correct exception is thrown when trying to delete a client by an ID that does not exist.
	 */
	@Test
	public void deleteClientByIdThrowsResourceNotFoundException() {
		doThrow(EmptyResultDataAccessException.class).when(clientRepo).deleteById(anyLong());

		assertThrows(ResourceNotFoundException.class, () -> clientService.deletebyId(1L));
	}

	/**
	 * This test checks if the correct exception is thrown when trying to delete a client but a database error occurs.
	 */
	@Test
	public void deleteClientByIdThrowsDatabaseException() {
		doThrow(DataIntegrityViolationException.class).when(clientRepo).deleteById(anyLong());

		assertThrows(DatabaseException.class, () -> clientService.deletebyId(1L));
	}
}