package passoff.dataaccess;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLUserDAO;
import dataaccess.UserDAO;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import requestsandresults.LoginRequest;
import requestsandresults.RegisterRequest;
import requestsandresults.RegisterResult;
import service.UserService;

import javax.xml.crypto.Data;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SQLDatabaseTests {
    UserDAO userDAO = new SQLUserDAO();
    UserData newUser = new UserData("Test", "password", "this@gmail.com");
    String existingAuth;
    AuthData newAuth = new AuthData( existingAuth, newUser.username());
    UserService userService = new UserService();

    public SQLDatabaseTests() throws DataAccessException {
    }

    @BeforeEach
    public void setup() throws DataAccessException { //TODO make sure it creates and deletes a test user
        RegisterResult regResult = userService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));
        existingAuth = regResult.authToken();

    }

    @AfterEach
    public void post() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    @DisplayName("Normal creation")
    public void register() throws DataAccessException {
        assertTrue(userDAO.verifyUser(new LoginRequest(newUser.username(), newUser.password())));
    }

    @Test
    @DisplayName("Duplicated user")
    public void dupeRegister(){
        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(newUser);
        },"Duplicate User not being caught");
    }

    @Test
    @DisplayName("Login")
    public void login() throws DataAccessException {
        Assertions.assertTrue(userDAO.verifyUser(new LoginRequest(newUser.username(), newUser.password())),
                "User not created");
    }

    @Test
    @DisplayName("Wrong username")
    public void badLogin() throws DataAccessException {
        assertThrows(DataAccessException.class, () ->{
            userDAO.verifyUser(new LoginRequest("Bob", newUser.password()));
        },"Not catching wrong username");
    }

    @Test
    public void clear() throws DataAccessException {//TODO create actual tests
        userDAO.clear();
    }
}
