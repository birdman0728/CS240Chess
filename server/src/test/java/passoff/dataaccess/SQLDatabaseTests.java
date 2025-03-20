package passoff.dataaccess;

import dataaccess.*;
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
import static org.junit.jupiter.api.Assertions.*;

public class SQLDatabaseTests {
    UserDAO userDAO = new SQLUserDAO();
    AuthDAO authDAO = new SQLAuthDAO();
    UserData newUser = new UserData("Test", "password", "this@gmail.com");
    String existingAuth;
    AuthData newAuth = new AuthData( existingAuth, newUser.username());
    UserService userService = new UserService();

    public SQLDatabaseTests() throws DataAccessException {
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        RegisterResult regResult = userService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));
        existingAuth = regResult.authToken();
    }

    @AfterEach
    public void post() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
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
    @DisplayName("Normal Auth Create")
    public void authCreate() throws DataAccessException {
        assertEquals(newAuth.username(), authDAO.getDataFromAuth(existingAuth).username(),
                "Auth not inserted properly");
    }

    @Test
    @DisplayName("Duplicate Auth Create")
    public void dupeAuth() throws DataAccessException {
        assertThrows(DataAccessException.class, () ->{
            authDAO.createAuth(new AuthData(existingAuth, newUser.username()));
        }, "Not catching duplicate entry");
    }

    @Test
    @DisplayName("Delete Auth Token")
    public void deleteAuth() throws DataAccessException {
        authDAO.deleteAuth(existingAuth);
        assertThrows(DataAccessException.class, () -> {
            authDAO.verifyAuth(existingAuth);
        },"Auth not deleted");
    }

    @Test
    public void clear() throws DataAccessException {//TODO create actual tests
        userDAO.clear();
        authDAO.clear();
    }
}
