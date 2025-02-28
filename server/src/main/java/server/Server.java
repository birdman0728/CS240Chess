package server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import requestsAndResults.RegisterRequest;
import requestsAndResults.RegisterResult;
import service.UserService;
import spark.*;

import java.io.IOException;
import java.io.InputStreamReader;

public class Server {
    private final UserService userService = new UserService();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        createRoutes();

        Spark.post("/user", this::Register);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object Register(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), RegisterRequest.class);
//        user = userService.register(req);
        //TODO: deal with error checking
        return new Gson().toJson(user);

        // var pet = new Gson().fromJson(req.body(), Pet.class);
        //        pet = service.addPet(pet);
        //        webSocketHandler.makeNoise(pet.name(), pet.sound());
        //        return new Gson().toJson(pet);
    }





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
