package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.ErrorResult;
import requestsandresults.*;
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
        Spark.delete("/session",this::logout);
//        Spark.get("/game",this::listGames);
        Spark.post("/game",this::createGame);
//        Spark.put("/game",this::joinGame);

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
    private Object logout(Request req, Response res) {
        String authToken = req.headers("authorization");

        LogoutRequest request = new LogoutRequest(authToken);
        try{
            return new Gson().toJson(userService.logout(request));
        }catch(DataAccessException e){
            res.status(401);
            return new Gson().toJson(new ErrorResult("Error: unauthorized"));
        }
    }

    private Object listGames(Request req, Response res) {
        ListRequest request = new ListRequest(req.headers("authorization"));
        try{
            userService.verifyAuth(request.authToken());
            return new Gson().toJson(gameService.listGames());
        }catch(DataAccessException e){
            res.status(401);
            return new Gson().toJson(new ErrorResult("Error: unauthorized"));
        }
    }
    private Object createGame(Request req, Response res) {
        String authToken = req.headers("authorization");
        var user = new Gson().fromJson(req.body(), CreateRequest.class);
        //TODO: check for bad request

        CreateRequest request = new CreateRequest(authToken, user.gameName());
        try{
            userService.verifyAuth(request.authToken());
            return new Gson().toJson(gameService.createGame(request));
        }catch(DataAccessException e){
            res.status(401);
            return new Gson().toJson(new ErrorResult("Error: unauthorized"));
        }
    }
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
