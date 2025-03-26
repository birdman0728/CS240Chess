package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import requestsandresults.*;

import static org.junit.jupiter.api.Assertions.*;
public class UserServiceTest {
    UserService userService = new UserService();
    UserData newUser = new UserData("Test", "password", "this@gmail.com");

    String existingAuth;

    AuthData newAuth = new AuthData( existingAuth, newUser.username());
    RegisterResult regResult;

    public UserServiceTest() throws DataAccessException {
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        userService.clear();

        regResult = userService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));
        existingAuth = regResult.authToken();
    }


    @Test
    @DisplayName("Successful register")
        public void register() throws DataAccessException {
//            RegisterResult registerResult = userService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));

            Assertions.assertEquals(newUser.username(), regResult.username(),
                "Response did not have the same username as was registered");
            Assertions.assertNotNull(regResult.authToken(), "Response did not contain an authentication string");
        }

    @Test
    @DisplayName("User Already Exists")
    public void loginInvalidUser(){
        RegisterRequest registerRequest = new RegisterRequest(newUser.username(), newUser.password(), newUser.email());

        assertThrows(DataAccessException.class, () -> {
            RegisterResult registerResult = userService.register(registerRequest);
            RegisterResult dupeResult = userService.register(registerRequest);
        });
    }

    @Test
    @DisplayName("Successful Login")
    void loginSuccess() throws DataAccessException {
        userService.clear();
        RegisterResult registerResult = userService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));
        LoginResult loginResult = userService.login(new LoginRequest(newUser.username(), newUser.password()));

        Assertions.assertEquals(newUser.username(), loginResult.username(),
                "Response did not give the same username as user");
        Assertions.assertNotNull(loginResult.authToken(), "Response did not return authentication String");
    }

    @Test
    @DisplayName("Not Correct User")
    void badAuth() throws DataAccessException {
        assertThrows(Exception.class, () -> {
            LoginResult loginResult = userService.login(new LoginRequest("Blah Blah", newUser.password()));
        });
    }

    @Test
    @DisplayName("Successful Logout")
    void logoutSuccess() throws DataAccessException {
        LogoutResult logoutResult = userService.logout(new LogoutRequest(existingAuth));
    }

    @Test
    @DisplayName("Unauthorized Logout")
    void logoutFailure() {
        assertThrows(Exception.class, () -> {
            LogoutResult logoutResult = userService.logout(new LogoutRequest("turd"));
        });
    }

    @Test
    void clear() throws DataAccessException {
        userService.clear();
        Assertions.assertTrue(userService.userDB.isEmpty(), "Server not empty");
        Assertions.assertTrue(userService.authDB.isEmpty(), "Server not empty");
    }
}