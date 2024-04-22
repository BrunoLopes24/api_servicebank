package com.brlopes.api_servicebank.Controller;

import com.brlopes.Controller.ClientController;
import com.brlopes.Model.Client;
import com.brlopes.Service.ClientService;
import com.brlopes.Service.exceptions.ErrorResponse;
import com.brlopes.Service.exceptions.NoClientIdException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class contains unit tests for the ClientController class.
 */
public class ClientControllerTest {

	/**
	 * The controller that is being tested.
	 */
	@InjectMocks
	private ClientController clientController;

	/**
	 * The service that the controller depends on.
	 */
	@Mock
	private ClientService clientService;

	/**
	 * The MockMvc instance used to perform HTTP requests.
	 */
	private MockMvc mockMvc;

	/**
	 * This method sets up the testing environment before each test.
	 */
	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
	}

	/**
	 * This test checks if the findAll method of the controller returns a list of clients.
	 */
	@Test
	@DisplayName("findAllReturnsListOfClients")
	public void findAllReturnsListOfClients() throws Exception {
		when(clientService.findAll()).thenReturn(Arrays.asList(new Client(), new Client()));
		mockMvc.perform(get("/client/"))
				.andExpect(status().isOk());
	}

	/**
	 * This test checks if the add method of the controller returns a BadRequest status when a client with minor age is added.
	 */
	@Test
	@DisplayName("addClientWithMinorAgeReturnsBadRequest")
	public void addClientWithMinorAgeReturnsBadRequest() throws Exception {
		Client client = new Client();
		client.setAge(17);
		when(clientService.insert(any(Client.class))).thenReturn(client);
		mockMvc.perform(post("/client/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(client)))
				.andExpect(status().isBadRequest());
	}

	/**
	 * This test checks if the findById method of the controller returns a BadRequest status when a non-existing ID is used.
	 */
	@Test
	void testFindByIdNonExistingId() throws Exception {
		// Given a non-existing client ID
		Long nonExistingId = 999L;

		// Mock clientService.findById to throw NoClientIdException
		when(clientService.findById(nonExistingId)).thenThrow(new NoClientIdException("Client not found in DataBase. Register first."));

		// When we call the endpoint /client/{id} with a non-existing ID
		ResponseEntity<?> responseEntity = clientController.findById(nonExistingId);

		// Then the response status should be BAD_REQUEST
		assertEquals(HttpStatus.BAD_REQUEST, ((ErrorResponse) responseEntity.getBody()).getStatus());

		// Also, we can use mockMvc to perform the request and check the status
		mockMvc.perform(get("/client/{id}", nonExistingId))
				.andExpect(status().isBadRequest());
	}

	/**
	 * This test checks if the deleteById method of the controller returns a NoContent status when a non-existing ID is used.
	 */
	@Test
	@DisplayName("deleteByIdWithNonExistingIdReturnsNoContent")
	public void deleteByIdWithNonExistingIdReturnsNoContent() throws Exception {
		mockMvc.perform(delete("/client/1"))
				.andExpect(status().isNoContent());
	}

	/**
	 * This test checks if the updateById method of the controller returns a BadRequest status when a non-existing ID is used.
	 */
	@Test
	@DisplayName("updateByIdWithNonExistingIdReturnsBadRequest")
	public void updateByIdWithNonExistingIdReturnsBadRequest() throws Exception {
		Client client = new Client();
		when(clientService.updateById(anyLong(), any(Client.class))).thenThrow(new RuntimeException());
		mockMvc.perform(put("/client/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(client)))
				.andExpect(status().isBadRequest());
	}
}