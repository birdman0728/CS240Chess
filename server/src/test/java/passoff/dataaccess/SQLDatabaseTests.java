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

import static org.junit.jupiter.api.Assertions.assertThrows;

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
//        RegisterResult regResult = userService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));
//        existingAuth = regResult.authToken();


//        """
//            CREATE TABLE IF NOT EXISTS  UserData (
//                `username` varchar(256) NOT NULL,
//                `password` varchar(256) NOT NULL,
//                `email` varchar(256) NOT NULL,
//                PRIMARY KEY (`username`)
//                )
//            """,
    }

    @AfterEach
    public void post(){
        var statement = "DELETE FROM userdata username = " + newUser.username();

    }

    @Test
    @DisplayName("Normal creation")
    public void register(){

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
}
