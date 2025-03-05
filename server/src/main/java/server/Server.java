package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.ErrorResult;
import RequestsAndResults.LoginRequest;
import RequestsAndResults.LogoutRequest;
import RequestsAndResults.RegisterRequest;
import RequestsAndResults.ClearResult;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private final UserService userService = new UserService();
    private final GameService gameService = new GameService();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);
        Spark.post("/session",this::login);
        Spark.delete("/session",this::Logout);
//        Spark.get("/game",this::ListGames);
//        Spark.post("/game",this::CreateGame);
//        Spark.put("/game",this::JoinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), RegisterRequest.class);
        if(user.username() == null || user.email() == null || user.password() == null){
            res.status(400);
//            res.body("Error: bad request");
            return new Gson().toJson(new ErrorResult("Error: bad request"));
        }

        RegisterRequest request = new RegisterRequest(user.username(),user.password(),user.email());
        try {
            return new Gson().toJson(userService.register(request));
        }catch(DataAccessException e ){
            res.status(403);
            res.body("Error: already taken");
            return new Gson().toJson(new ErrorResult("Error: already taken"));
        }
    }

    private Object clear(Request req, Response res) {
        userService.clear();
        gameService.clear();

        return new Gson().toJson(new ClearResult());
    }

    private Object login(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginRequest request = new LoginRequest(user.username(),user.password());

        try {
            return new Gson().toJson(userService.login(request));
        }catch(DataAccessException e){
            res.status(401);
            return new Gson().toJson(new ErrorResult("Error: unauthorized"));
        }
    }
    private Object Logout(Request req, Response res) {
//        for(String authToken : req.headers()){
            var user = new Gson().fromJson(req.headers(), LogoutRequest.class); //TODO figure out how to deserialize out of headers
//            String test = "testicles";
//        }

        LogoutRequest request = new LogoutRequest(user.authToken());
        return null;
    }

    private Object listGames(Request req, Response res) {
        return null;}
    private Object createGame(Request req, Response res) {
        return null;}
    private Object joinGame(Request req, Response res) {
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
