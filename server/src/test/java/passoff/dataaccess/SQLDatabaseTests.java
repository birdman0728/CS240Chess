package passoff.dataaccess;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SQLUserDAO;
import dataaccess.UserDAO;
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

    public SQLDatabaseTests() throws DataAccessException {
    }

    @BeforeEach
    public void setup() throws DataAccessException {


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
        });
    }

    @Test
    @DisplayName("Login")
    public void login() throws DataAccessException {
        Assertions.assertTrue(userDAO.verifyUser(new LoginRequest(newUser.username(), newUser.password())),
                "User not created");
    }
}
