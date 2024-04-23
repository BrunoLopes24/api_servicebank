package com.brlopes.api_servicebank.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.brlopes.Controller.TransactionController;
import com.brlopes.Model.Transactions;
import com.brlopes.Service.TransactionsService;
import com.brlopes.dto.TransactionDTO;

/**
 * This class contains unit tests for the TransactionController class.
 * It uses JUnit 5 and Mockito for testing.
 */
public class TransactionControllerTest {

	// The class under test
	@InjectMocks
	TransactionController transactionController;

	// The mocked service class
	@Mock
	TransactionsService transactionsService;

	/**
	 * This method sets up the mocks before each test.
	 */
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * This test checks the findAll method of the TransactionController class.
	 * It verifies that the method returns a status of OK and calls the findAll method of the service class exactly once.
	 */
	@Test
	@DisplayName("Test findAll Transactions")
	public void testFindAll() {
		// Arrange
		Transactions transaction = new Transactions();
		when(transactionsService.findAll()).thenReturn(List.of(transaction));

		// Act
		ResponseEntity<?> response = transactionController.findAll();

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(transactionsService, times(1)).findAll();
	}

	/**
	 * This test checks the addTransaction method of the TransactionController class with a valid request.
	 * It verifies that the method returns a status of OK and calls the insert method of the service class exactly once.
	 */
	@Test
	@DisplayName("Test addTransaction with valid request")
	public void testAddTransaction_ValidRequest() {
		// Arrange
		TransactionDTO request = new TransactionDTO();
		request.setClientName("Client");
		request.setDestinyClientName("DestinyClient");

		// Act
		ResponseEntity<?> response = transactionController.addTransaction(request);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(transactionsService, times(1)).insert(any(), anyString(), anyString());
	}

	/**
	 * This test checks the addTransaction method of the TransactionController class with an invalid request.
	 * It verifies that the method returns a status of BAD_REQUEST.
	 */
	@Test
	@DisplayName("Test addTransaction with invalid request")
	public void testAddTransaction_InvalidRequest() {
		// Arrange
		TransactionDTO request = new TransactionDTO();

		// Act
		ResponseEntity<?> response = transactionController.addTransaction(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	/**
	 * This test checks the findById method of the TransactionController class with a valid id.
	 * It verifies that the method returns a status of OK and calls the findById method of the service class exactly once.
	 */
	@Test
	@DisplayName("Test findById with valid id")
	public void testFindById_ValidId() {
		// Arrange
		Long id = 1L;
		Transactions transaction = new Transactions();
		when(transactionsService.findById(id)).thenReturn(transaction);

		// Act
		ResponseEntity<?> response = transactionController.findById(id);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(transactionsService, times(1)).findById(id);
	}

	/**
	 * This test checks the deleteById method of the TransactionController class with a valid id.
	 * It verifies that the method returns a status of NO_CONTENT and calls the deleteById method of the service class exactly once.
	 */
	@Test
	@DisplayName("Test deleteById with valid id")
	public void testDeleteById_ValidId() {
		// Arrange
		Long id = 1L;
		doNothing().when(transactionsService).deletebyId(id);

		// Act
		ResponseEntity<?> response = transactionController.deletebyId(id);

		// Assert
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		verify(transactionsService, times(1)).deletebyId(id);
	}

	/**
	 * This test checks the addDeposit method of the TransactionController class with a valid request.
	 * It verifies that the method returns a status of OK and calls the addDeposit method of the service class exactly once.
	 */
	@Test
	@DisplayName("Test addDeposit with valid request")
	public void testAddDeposit_ValidRequest() {
		// Arrange
		Transactions transaction = new Transactions();
		String clientName = "Client";
		when(transactionsService.addDeposit(transaction, clientName)).thenReturn(transaction);

		// Act
		ResponseEntity<?> response = transactionController.addDeposit(transaction, clientName);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(transactionsService, times(1)).addDeposit(transaction, clientName);
	}
}