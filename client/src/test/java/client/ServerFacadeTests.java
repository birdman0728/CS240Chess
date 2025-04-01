package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
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
        sF.register(new RegisterRequest(testUser,password,email));
        authToken = sF.login(new LoginRequest(testUser, password)).authToken();
        gameID = sF.create(new CreateRequest(authToken, gameName)).gameID();
    }

    @AfterAll
    static void stopServer() {
        sF.clear();
        server.stop();
    }

//    @Test
//    public void sampleTest() {
//        Assertions.assertTrue(true);
//    }

    @Test
    public void normalReg(){
        sF.clear();
        Assertions.assertNotNull(sF.register(new RegisterRequest(testUser,password,email)), "Not properly returning a Register Result");
    }

    @Test
    public void missingInfoReg(){
        Assertions.assertNull(sF.register(new RegisterRequest(testUser,null,email)),"Does not throw error");
    }

    @Test
    public void normalLogin(){
        Assertions.assertNotNull(sF.login(new LoginRequest(testUser, password)).authToken(),"Error in logging in correctly");
    }

    @Test
    public void badLogin(){
        Assertions.assertNull(sF.login(new LoginRequest("not correct", "also not correct")),"Not catching bad login");
    }

    @Test
    public void badAuthLogout(){
        Assertions.assertNull(sF.logout(new LogoutRequest("alkn3j9fnsljfs")),"Not catching a bad logout");
    }

    @Test
    public void goodList(){
        Assertions.assertNotNull(sF.listGames(new ListRequest(authToken)), "Not listing correctly");
    }

    @Test
    public void badList(){
        Assertions.assertNull(sF.listGames(new ListRequest("a;kjfdsals;jf")), "Not rejecting bad auth");
    }

    @Test
    public void goodCreate(){
        Assertions.assertNotNull(sF.create(new CreateRequest(authToken, gameName)));
    }

    @Test
    public void badCreate(){
        Assertions.assertNull(sF.create(new CreateRequest("fas;dkj", gameName)));
    }

    @Test
    public void goodJoin(){
        Assertions.assertNotNull(sF.join(new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID, testUser)));
    }

    @Test
    public void badJoin(){
        sF.join(new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID, "other User"));
        Assertions.assertNull(sF.join(new JoinRequest(authToken, ChessGame.TeamColor.WHITE, gameID, testUser)));
    }

    @Test
    public void normalLogout(){
        Assertions.assertEquals(new LogoutResult(), sF.logout(new LogoutRequest(authToken)),"Not logging out correctly");
    }

    @Test
    public void clear(){
        Assertions.assertNotNull(sF.clear());
    }
}
