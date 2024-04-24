package com.brlopes.api_servicebank.Service;

import com.brlopes.Model.Client;
import com.brlopes.Model.Transactions;
import com.brlopes.Repository.ClientRepo;
import com.brlopes.Repository.TransactionRepo;
import com.brlopes.Service.TransactionsService;
import com.brlopes.Service.exceptions.DataIntegrityViolationException;
import com.brlopes.Service.exceptions.DatabaseException;
import com.brlopes.Service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * This class is used to test the TransactionsService class.
 * It uses Mockito to mock the dependencies of the TransactionsService class.
 */
public class TransactionServiceTest {

	@InjectMocks
	private TransactionsService transactionsService;

	@Mock
	private TransactionRepo transactionRepo;

	@Mock
	private ClientRepo clientRepo;

	/**
	 * This method is executed before each test.
	 * It initializes the mocks.
	 */
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * This test checks if a transaction can be inserted successfully.
	 */
	@Test
	public void insertTransactionSuccessfully() {
		Transactions transaction = new Transactions();
		transaction.setValue(500.0); // Set a value before using the transaction
		transaction.setTax(50.0); // Set a tax value before using the transaction
		Client client = new Client();
		client.setClient_id(1L); // Setting a non-null ID for the client
		client.setBalance(1000.0);

		Client destinyClient = new Client();
		destinyClient.setClient_id(2L); // Setting a different non-null ID for the destiny client
		destinyClient.setBalance(500.0);

		when(clientRepo.findByName("clientName")).thenReturn(client);
		when(clientRepo.findByName("destinyClientName")).thenReturn(destinyClient);
		when(transactionRepo.save(any(Transactions.class))).thenReturn(transaction);

		Transactions result = transactionsService.insert(transaction, "clientName", "destinyClientName");

		assertEquals(transaction, result);
	}

	/**
	 * This test checks if all transactions can be fetched successfully.
	 */
	@Test
	public void findAllTransactionsSuccessfully() {
		transactionsService.findAll();

		verify(transactionRepo, times(1)).findAll();
	}

	/**
	 * This test checks if a transaction can be fetched by its ID successfully.
	 */
	@Test
	public void findTransactionByIdSuccessfully() {
		Transactions transaction = new Transactions();
		when(transactionRepo.findById(anyLong())).thenReturn(Optional.of(transaction));

		Transactions result = transactionsService.findById(1L);

		assertEquals(transaction, result);
	}

	/**
	 * This test checks if the correct exception is thrown when a transaction is not found by its ID.
	 */
	@Test
	public void findTransactionByIdThrowsResourceNotFoundException() {
		when(transactionRepo.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> transactionsService.findById(1L));
	}

	/**
	 * This test checks if a transaction can be deleted by its ID successfully.
	 */
	@Test
	public void deleteTransactionByIdSuccessfully() {
		doNothing().when(transactionRepo).deleteById(anyLong());

		assertDoesNotThrow(() -> transactionsService.deletebyId(1L));
	}

	/**
	 * This test checks if the correct exception is thrown when a database error occurs during deletion of a transaction.
	 */
	@Test
	public void deleteTransactionByIdThrowsResourceNotFoundException() {
		doThrow(EmptyResultDataAccessException.class).when(transactionRepo).deleteById(anyLong());

		assertThrows(ResourceNotFoundException.class, () -> transactionsService.deletebyId(1L));
	}

	/**
	 * This test checks if the correct exception is thrown when a database error occurs during deletion of a transaction.
	 */
	@Test
	public void deleteTransactionByIdThrowsDatabaseException() {
		doThrow(DataIntegrityViolationException.class).when(transactionRepo).deleteById(anyLong());

		assertThrows(DatabaseException.class, () -> transactionsService.deletebyId(1L));
	}

	/**
	 * This test checks if a deposit can be added successfully.
	 */
	@Test
	public void addDepositSuccessfully() {
		Transactions transaction = new Transactions();
		transaction.setValue(500.0); // Set a value before using the transaction
		Client client = new Client();
		client.setClient_id(1L); // Setting a non-null ID for the client
		client.setBalance(1000.0);

		when(clientRepo.findByName("clientName")).thenReturn(client);
		when(transactionRepo.save(any(Transactions.class))).thenReturn(transaction);

		Transactions result = transactionsService.addDeposit(transaction, "clientName");

		assertEquals(transaction, result);
	}

	/**
	 * This test checks if the correct exception is thrown when a deposit cannot be added due to a data integrity violation.
	 */
	@Test
	public void addDepositThrowsDataIntegrityViolationException() {
		Transactions transaction = new Transactions();
		when(clientRepo.findByName(anyString())).thenReturn(null);

		assertThrows(DataIntegrityViolationException.class, () -> transactionsService.addDeposit(transaction, "clientName"));
	}
}