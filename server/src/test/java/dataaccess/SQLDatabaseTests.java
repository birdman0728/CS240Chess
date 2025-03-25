package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.*;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import requestsandresults.*;
import service.UserService;

import javax.xml.crypto.Data;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.junit.jupiter.api.Assertions.*;

public class SQLDatabaseTests {
    UserDAO userDAO = new SQLUserDAO();
    AuthDAO authDAO = new SQLAuthDAO();
    GameDAO gameDAO = new SQLGameDAO();
    UserData newUser = new UserData("Test", "password", "this@gmail.com");
    String existingAuth;
    AuthData newAuth = new AuthData( existingAuth, newUser.username());
    UserService userService = new UserService();
    String gameName = "TestGame";
    int gameID;

    public SQLDatabaseTests() throws DataAccessException {
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        RegisterResult regResult = userService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));
        existingAuth = regResult.authToken();
        gameID = gameDAO.createGame(gameName);
    }

    @AfterEach
    public void post() throws DataAccessException {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
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
    @DisplayName("Creating Game")
    public void createGame() throws DataAccessException {
        assertEquals(gameName, gameDAO.getGame(gameID).gameName(),
                "Failed to create game");
    }

    @Test
    @DisplayName("getting game")
    public void getGame() throws DataAccessException {
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        assertEquals(newGame, gameDAO.getGame(gameID));
    }

    @Test
    @DisplayName("get bad game")
    public void getBadGame() throws DataAccessException {
        assertThrows(DataAccessException.class, ()->{
            gameDAO.getGame(-1);
        },("Does not throw error"));
    }

    @Test
    @DisplayName("List all games")
    public void listAllGames() throws DataAccessException{
        gameDAO.createGame("2");
        gameDAO.createGame("3");
        gameDAO.createGame("4");
        gameDAO.createGame("5");
        assertEquals(5, gameDAO.getAllGames().size(),
                "Does not list all the games");
    }

    @Test
    @DisplayName("List 0 games")
    public void listnogames() throws DataAccessException{
        gameDAO.clear();
        assertEquals(0, gameDAO.getAllGames().size(), "not listing 0 games");
    }



    @Test
    @DisplayName("Successful Join Game")
    public void joinGame() throws DataAccessException{
        gameDAO.joinGame(new JoinRequest(existingAuth, ChessGame.TeamColor.BLACK, gameID, "blackJoined"));
        gameDAO.joinGame(new JoinRequest(existingAuth, ChessGame.TeamColor.WHITE, gameID, "whiteJoined"));
        GameData endData = new GameData(gameID, "whiteJoined", "blackJoined", gameName, new ChessGame());
        assertEquals(endData, gameDAO.getGame(gameID),"usernames not updating");
    }

    @Test
    @DisplayName("Game Doesn't exist")
    public void failJoinGame() throws DataAccessException{
        assertThrows(DataAccessException.class, ()->{
            gameDAO.joinGame(new JoinRequest(existingAuth, ChessGame.TeamColor.WHITE, -1, "ladiesman217"));
        },"bad request");

    }

    @Test
    @DisplayName("Successful Update game")
    public void updateGame() throws InvalidMoveException, DataAccessException {
        ChessGame testGame = new ChessGame();
        testGame.makeMove(new ChessMove(new ChessPosition(2,2), new ChessPosition(3,2), null));
        gameDAO.updateGame(new GameData(1,"irrelevant", "irrelevant", "irrelevant", testGame), gameID);
    }

    @Test
    @DisplayName("Doesn't update game")
    public void failedUpdate(){
        assertThrows(DataAccessException.class, ()->{
            gameDAO.updateGame(new GameData(-1, "", "", "bob", new ChessGame()), -1);
        },"bad request");
    }

    @Test
    @DisplayName("Empty After Clear")
    void checkEmpty() throws DataAccessException {
        clear();
        Assertions.assertTrue(authDAO.isEmpty(), "Server not empty");
        Assertions.assertTrue(userDAO.isEmpty(), "Server not empty");
        Assertions.assertTrue(gameDAO.isEmpty(), "Server not empty");
    }
    @Test
    @DisplayName("Check Non-Empty Servers")
    void notEmptyServers() throws DataAccessException{
        Assertions.assertFalse(authDAO.isEmpty(), "Server not empty");
        Assertions.assertFalse(userDAO.isEmpty(), "Server not empty");
        Assertions.assertFalse(gameDAO.isEmpty(), "Server not empty");
    }

    @Test
    public void clear() throws DataAccessException {//TODO create actual tests
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
        assertTrue(userDAO.isEmpty(),"user table not cleared");
        assertTrue(authDAO.isEmpty(),"auth table not cleared");
        assertTrue(gameDAO.isEmpty(),"Game table not cleared");
    }
}
