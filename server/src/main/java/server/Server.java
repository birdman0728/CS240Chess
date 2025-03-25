package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.ErrorResult;
import requestsandresults.*;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {
    private UserService userService = null;
    private GameService gameService = null;

    public Server() {
        try{
            userService = new UserService();
            gameService = new GameService();
        }catch(DataAccessException e){
            throw new RuntimeException();
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::register);
        Spark.delete("/db", this::clear);
        Spark.post("/session",this::login);
        Spark.delete("/session",this::logout);
        Spark.get("/game",this::listGames);
        Spark.post("/game",this::createGame);
        Spark.put("/game",this::joinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), RegisterRequest.class);
        if(user.username() == null || user.email() == null || user.password() == null){
            res.status(400);
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

    private Object clear(Request req, Response res) throws DataAccessException {
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
            return new Gson().toJson(unauthorizedError());
        }
    }
    private Object logout(Request req, Response res) {
        String authToken = getAuth(req);

        LogoutRequest request = new LogoutRequest(authToken);
        try{
            return new Gson().toJson(userService.logout(request));
        }catch(DataAccessException e){
            res.status(401);
            return new Gson().toJson(unauthorizedError());
        }
    }

    private Object listGames(Request req, Response res) {
        ListRequest request = new ListRequest(getAuth(req));

        try{
            userService.verifyAuth(request.authToken());
            return new Gson().toJson(gameService.listGames());
        }catch(DataAccessException e){
            res.status(401);
            return new Gson().toJson(unauthorizedError());
        }
    }
    private Object createGame(Request req, Response res) {
        String authToken = getAuth(req);
        var user = new Gson().fromJson(req.body(), CreateRequest.class);
        if(user.gameName() == null){
            res.status(400);
            return new Gson().toJson(new ErrorResult("Error: bad request"));
        }


        CreateRequest request = new CreateRequest(authToken, user.gameName());
        try{
            userService.verifyAuth(request.authToken());
            return new Gson().toJson(gameService.createGame(request));
        }catch(DataAccessException e){
            res.status(401);
            return new Gson().toJson(unauthorizedError());
        }
    }
    private Object joinGame(Request req, Response res) {
        String authToken = getAuth(req);
        var user = new Gson().fromJson(req.body(), JoinRequest.class);



        if(user.playerColor() == null || user.gameID() == 0){
            res.status(400);
            return new Gson().toJson(new ErrorResult("Error: bad request"));
        }

        try{
            JoinRequest request = new JoinRequest(authToken, user.playerColor(), user.gameID(), userService.getUser(authToken).username());
            userService.verifyAuth(authToken);
//            res.status(200);
            return new Gson().toJson(gameService.joinGame(request));
        }catch(DataAccessException e){
            if(e.getMessage().equals("Error: already Taken")){
                res.status(403);
                return new Gson().toJson(new ErrorResult(e.getMessage()));
            }else {
                res.status(401);
                return new Gson().toJson(unauthorizedError());
            }
        }
    }

    private String getAuth(Request req){
        return req.headers("authorization");
    }

    private ErrorResult unauthorizedError(){
        return new ErrorResult("Error: unauthorized");
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
