package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoff.model.TestCreateResult;
import requestsandresults.*;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    GameService gameService = new GameService();
    UserService userService = new UserService();
    String existingAuth;
    UserData newUser = new UserData("Test", "password", "this@gmail.com");
    GameData newGame = new GameData(1, "WhiteTest", "BlackTest", "Test", new ChessGame());

    @BeforeEach
    public void setup() throws DataAccessException {
        userService.clear();
        gameService.clear();

        RegisterResult regResult = userService.register(new RegisterRequest(newUser.username(), newUser.password(), newUser.email()));
        existingAuth = regResult.authToken();

        CreateResult createResult = gameService.createGame(new CreateRequest(existingAuth,"Test"));
    }

    @Test
    @DisplayName("Successful Game Creation")
    void createGame() throws DataAccessException {
        CreateResult createResult = gameService.createGame(new CreateRequest(existingAuth,"Test"));

        Assertions.assertEquals(createResult.gameID(), createResult.gameID(), "Mismatched gameID's.");
    }

    @Test
    @DisplayName("Failed Game Creation")
    void unauthenticated() throws DataAccessException {
        userService.logout(new LogoutRequest(existingAuth));

        assertThrows(DataAccessException.class, () -> {
            userService.verifyAuth(existingAuth);
        });
    }

    @Test
    void successJoinGame() throws DataAccessException {
        JoinResult joinResult = gameService.joinGame(new JoinRequest(existingAuth, ChessGame.TeamColor.WHITE, newGame.gameID(), newUser.username()));

        Assertions.assertEquals(gameService.gameDB.getGame(newGame.gameID()).whiteUsername(), newUser.username()," User did not join game");
    }

    @Test
    void failedJoinGame() throws DataAccessException {
        userService.logout(new LogoutRequest(existingAuth));

        assertThrows(DataAccessException.class, () -> {
            userService.verifyAuth(existingAuth);
        });
    }

    @Test
    void successfulListGames() throws DataAccessException {
        gameService.createGame(new CreateRequest(existingAuth,"Test"));

        ListResult listResult = gameService.listGames();
        Assertions.assertEquals(listResult.games().size(), 2, "Improper games added");
    }

    @Test
    void cantListGames() throws DataAccessException {
        userService.logout(new LogoutRequest(existingAuth));

        assertThrows(Exception.class, () -> {
            gameService.listGames();
            userService.verifyAuth(existingAuth);
        });
    }

    @Test
    void clear() {
        userService.clear();
        gameService.clear();
        Assertions.assertTrue(userService.userDB.isEmpty(), "Server not empty");
        Assertions.assertTrue(userService.authDB.isEmpty(), "Server not empty");
        Assertions.assertTrue(gameService.gameDB.isEmpty(), "Server not empty");

    }
}