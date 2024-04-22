package com.brlopes.api_servicebank.Controller;

import com.brlopes.Controller.auth.AuthController;
import com.brlopes.Model.Login;
import com.brlopes.Model.enums.LoginRoles;
import com.brlopes.Repository.LoginRepo;
import com.brlopes.Service.authService.TokenService;
import com.brlopes.dto.AuthenticationDTO;
import com.brlopes.dto.registerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * This class is used to test the AuthController class.
 */
public class AuthControllerTest {

    // The AuthController instance to be tested
    @InjectMocks
    private AuthController authController;

    // Mocked AuthenticationManager instance
    @Mock
    private AuthenticationManager authManager;

    // Mocked LoginRepo instance
    @Mock
    private LoginRepo loginRepo;

    // Mocked TokenService instance
    @Mock
    private TokenService tokenService;

    /**
     * This method is executed before each test. It initializes the mocks.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * This test checks if the login method of the AuthController class returns Unauthorized status when the credentials are invalid.
     */
    @Test
    public void loginShouldReturnUnauthorizedWhenCredentialsAreInvalid() {
        AuthenticationDTO authDTO = new AuthenticationDTO("invalidUser", "invalidPassword");

        when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<?> response = authController.login(authDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * This test checks if the login method of the AuthController class returns Unauthorized status when the user is not found.
     */
    @Test
    public void loginShouldReturnUnauthorizedWhenUserNotFound() {
        AuthenticationDTO authDTO = new AuthenticationDTO("nonExistentUser", "testPassword");

        when(authManager.authenticate(any())).thenThrow(new UsernameNotFoundException("User not found"));

        ResponseEntity<?> response = authController.login(authDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    /**
     * This test checks if the login method of the AuthController class returns a token when the credentials are valid.
     */
    @Test
    public void loginShouldReturnTokenWhenCredentialsAreValid() {
        AuthenticationDTO authDTO = new AuthenticationDTO("testUser", "testPassword");
        Login login = new Login("testUser", "testPassword", LoginRoles.CLIENT);
        Authentication auth = new UsernamePasswordAuthenticationToken(login, null, login.getAuthorities());

        when(authManager.authenticate(any())).thenReturn(auth);
        when(tokenService.generateToken(any())).thenReturn("testToken");

        ResponseEntity<?> response = authController.login(authDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * This test checks if the register method of the AuthController class returns a BadRequest status when the user already exists.
     */
    @Test
    public void registerShouldReturnBadRequestWhenUserAlreadyExists() {
        registerDTO regDTO = new registerDTO("testUser", "testPassword", LoginRoles.CLIENT);

        when(loginRepo.findByUsername(anyString())).thenReturn(new Login());

        ResponseEntity<?> response = authController.register(regDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * This test checks if the register method of the AuthController class returns an Ok status when the user does not exist.
     */
    @Test
    public void registerShouldReturnOkWhenUserDoesNotExist() {
        registerDTO regDTO = new registerDTO("testUser", "testPassword", LoginRoles.CLIENT);

        when(loginRepo.findByUsername(anyString())).thenReturn(null);

        ResponseEntity<?> response = authController.register(regDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * This test checks if the register method of the AuthController class returns a BadRequest status when the data is invalid.
     */
    @Test
    public void registerShouldReturnBadRequestWhenDataIsInvalid() {
        registerDTO regDTO = new registerDTO("", "", LoginRoles.CLIENT);

        ResponseEntity<?> response = authController.register(regDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}