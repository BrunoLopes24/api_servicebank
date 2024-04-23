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

public class TransactionControllerTest {
	
	@InjectMocks
	TransactionController transactionController;
	
	@Mock
	TransactionsService transactionsService;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
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