package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.server.Authentication;
import requestsAndResults.RegisterRequest;
import requestsAndResults.RegisterResult;
import requestsAndResults.clearResult;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStreamReader;

public class Server {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();
    private final ClearService clearService = new ClearService();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        createRoutes();

        Spark.post("/user", this::Register);
        Spark.delete("/db", this::Clear);
//        Spark.post("/session",this::Login);
//        Spark.delete("/session",this::Logout);
//        Spark.get("/game",this::ListGames);
//        Spark.post("/game",this::CreateGame);
//        Spark.put("/game",this::JoinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object Register(Request req, Response res) {
        RegisterRequest user = new Gson().fromJson(req.body(), RegisterRequest.class);

//        try {
////        user = userService.register(user);
//        }catch(DataAccessException e ){
//            //TODO: deal with error checking
//        }
        return new Gson().toJson(user);
    }

    private Object Clear(Request req, Response res) {
//        clearService.clearAll();
        userService.clear();
        gameService.clear();

        return new Gson().toJson(new clearResult());//TODO add response
    }
    private Object Login(Request req, Response res) {
        return null;}
    private Object Logout(Request req, Response res) {
        return null;}
    private Object ListGames(Request req, Response res) {
        return null;}
    private Object CreateGame(Request req, Response res) {
        return null;}
    private Object JoinGame(Request req, Response res) {
        return null;}




    // var pet = new Gson().fromJson(req.body(), Pet.class);
    //        pet = service.addPet(pet);
    //        webSocketHandler.makeNoise(pet.name(), pet.sound());
    //        return new Gson().toJson(pet);





//    private static void createRoutes() {
//
//        //register
//        Spark.post("/user", ((request, response) -> {
//
//            //            response.status(200);
////            response.type("Register a new user");
////            response.body("username":"","password":"", "email":"")
//        }));
//
//
//    }

//    private string

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
