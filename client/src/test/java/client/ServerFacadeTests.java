package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.ResponseException;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.*;
import requestsandresults.*;
import server.Server;
import Server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sF;

    private static final String testUser = "Test";
    private static final String password = "password";
    private static final String email = "email@email.com";
    private static String authToken;
    private static int gameID;
    private static final String gameName = "name";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        sF = new ServerFacade("http://localhost:" + port);
    }
    @BeforeEach
    public void prep() throws ResponseException {
        sF.register(new RegisterRequest(testUser,password,email));
        authToken = sF.login(new LoginRequest(testUser, password)).authToken();
        gameID = sF.create(new CreateRequest(authToken, gameName)).gameID();
    }

    @AfterEach
    public void cleanup() throws ResponseException {
        sF.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void normalReg() throws ResponseException {
        Assertions.assertNotNull(sF.register(new RegisterRequest("newguy",password,email)), "Not properly returning a Register Result");
    }

    @Test
    public void missingInfoReg() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> {
                sF.register(new RegisterRequest(testUser,null,email));
                },"Does not throw error");
    }

    @Test
    public void normalLogin() throws ResponseException {
        Assertions.assertNotNull(sF.login(new LoginRequest(testUser, password)).authToken(),"Error in logging in correctly");
    }

    @Test
    public void badLogin() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> {
            sF.login(new LoginRequest("not correct", "also not correct"));
        },"Not catching bad login");
    }

    @Test
    public void badAuthLogout() throws ResponseException {
            Assertions.assertThrows(ResponseException.class, () -> {
                sF.logout(new LogoutRequest("alkn3j9fnsljfs"));
            },"Not catching a bad logout");
    }

    @Test
    public void goodList() throws ResponseException {
        Assertions.assertNotNull(sF.listGames(new ListRequest(authToken)), "Not listing correctly");
    }

    @Test
    public void badList() throws ResponseException {
            Assertions.assertThrows(ResponseException.class, () -> {
                sF.listGames(new ListRequest("a;kjfdsals;jf"));
            }, "Not rejecting bad auth");
    }

    @Test
    public void goodCreate() throws ResponseException {
        Assertions.assertNotNull(sF.create(new CreateRequest(authToken, gameName)));
    }

    @Test
    public void badCreate() throws ResponseException {
            Assertions.assertThrows(ResponseException.class, () -> {
                sF.create(new CreateRequest("fas;dkj", gameName));
            },"Not catching a bad authToken");
    }

    @Test
    public void goodJoin() throws ResponseException {
        Assertions.assertNotNull(sF.join(new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID, testUser)));
    }

    @Test
    public void badJoin() throws ResponseException {
        sF.join(new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID, "other User"));
            Assertions.assertThrows(ResponseException.class, () -> {
                sF.join(new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID, testUser));
            },"Not using the correct username");
    }

    @Test
    public void normalLogout() throws ResponseException {
        Assertions.assertEquals(new LogoutResult(), sF.logout(new LogoutRequest(authToken)),"Not logging out correctly");
    }

    @Test
    public void clear() throws ResponseException {
        Assertions.assertNotNull(sF.clear());
    }
}
