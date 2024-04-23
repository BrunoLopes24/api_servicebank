package com.brlopes.api_servicebank.Service.Auth;

import com.brlopes.Model.Login;
import com.brlopes.Service.authService.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class contains unit tests for the TokenService class.
 */
public class TokenServiceTest {

	/**
	 * The TokenService instance that is being tested.
	 */
	@InjectMocks
	private TokenService tokenService;

	/**
	 * The mocked Login instance for testing.
	 */
	@Mock
	private Login login;

	/**
	 * This method sets up the testing environment before each test.
	 */
	@BeforeEach
	public void setup() {
		// Initialize the mocks
		MockitoAnnotations.openMocks(this);
		// Set the secret field in the tokenService instance
		ReflectionTestUtils.setField(tokenService, "secret", "testSecret");
	}

	/**
	 * This test checks if the generateToken method returns a valid token.
	 */
	@Test
	public void generateTokenReturnsValidToken() {
		String username = "testUser";
		// Mock the getUsername method of the login instance
		Mockito.when(login.getUsername()).thenReturn(username);
		// Generate a token
		String token = tokenService.generateToken(login);
		// Validate the token
		String subject = tokenService.validateToken(token);
		// Check if the subject of the token is the same as the username
		assertEquals(username, subject);
	}

	/**
	 * This test checks if the validateToken method returns an empty string for an invalid token.
	 */
	@Test
	public void validateTokenReturnsEmptyStringForInvalidToken() {
		String invalidToken = "invalidToken";
		// Validate the invalid token
		String subject = tokenService.validateToken(invalidToken);
		// Check if the subject of the token is an empty string
		assertEquals("", subject);
	}

	/**
	 * This test checks if the generateToken method throws an exception when a null Login object is passed.
	 */
	@Test
	public void generateTokenThrowsExceptionForNullLogin() {
		// Check if a RuntimeException is thrown when a null Login object is passed to the generateToken method
		assertThrows(RuntimeException.class, () -> tokenService.generateToken(null));
	}
}