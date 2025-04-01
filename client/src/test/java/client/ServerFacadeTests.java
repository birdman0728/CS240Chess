package client;

import dataaccess.DataAccessException;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.*;
import requestsandresults.LoginRequest;
import requestsandresults.LoginResult;
import requestsandresults.RegisterRequest;
import server.Server;
import Server.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade sF;

    private static final String testUser = "Test";
    private static final String password = "password";
    private static final String email = "email@email.com";
    private static String authToken;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        sF = new ServerFacade("http://localhost:" + port);
        sF.register(new RegisterRequest(testUser,password,email));
        authToken = sF.login(new LoginRequest(testUser, password)).authToken();
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
        Assertions.assertNotNull(sF.login(new LoginRequest(testUser, password)).authToken());
    }

}
