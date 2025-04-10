package client;

import chess.ChessGame;
import model.ResponseException;
import org.junit.jupiter.api.*;
import requestsandresults.*;
import server.Server;
import serverFacade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sF;

    private static final String TEST_USER = "Test";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email@email.com";
    private static String authToken;
    private static int gameID;
    private static final String GAME_NAME = "name";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        sF = new ServerFacade("http://localhost:" + port);
    }
    @BeforeEach
    public void prep() throws ResponseException {
        sF.register(new RegisterRequest(TEST_USER, PASSWORD, EMAIL));
        authToken = sF.login(new LoginRequest(TEST_USER, PASSWORD)).authToken();
        gameID = sF.create(new CreateRequest(authToken, GAME_NAME)).gameID();
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
        Assertions.assertNotNull(sF.register(new RegisterRequest("newguy", PASSWORD, EMAIL)), "Not properly returning a Register Result");
    }

    @Test
    public void missingInfoReg() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> {
                sF.register(new RegisterRequest(TEST_USER,null, EMAIL));
                },"Does not throw error");
    }

    @Test
    public void normalLogin() throws ResponseException {
        Assertions.assertNotNull(sF.login(new LoginRequest(TEST_USER, PASSWORD)).authToken(),"Error in logging in correctly");
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
        Assertions.assertNotNull(sF.create(new CreateRequest(authToken, GAME_NAME)));
    }

    @Test
    public void badCreate() throws ResponseException {
            Assertions.assertThrows(ResponseException.class, () -> {
                sF.create(new CreateRequest("fas;dkj", GAME_NAME));
            },"Not catching a bad authToken");
    }

    @Test
    public void goodJoin() throws ResponseException {
        Assertions.assertNotNull(sF.join(new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID, TEST_USER)));
    }

    @Test
    public void badJoin() throws ResponseException {
        sF.join(new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID, "other User"));
            Assertions.assertThrows(ResponseException.class, () -> {
                sF.join(new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID, TEST_USER));
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
