package server;

import com.google.gson.Gson;
import requestsAndResults.RegisterRequest;
import requestsAndResults.RegisterResult;
import spark.*;

import java.io.IOException;
import java.io.InputStreamReader;

public class Server {

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

    private Object Register(Request req, Response res) throws IOException {
        var serializer = new Gson();
        InputStreamReader inputStreamReader = new InputStreamReader(req.raw().getInputStream());
        RegisterRequest  newReq = serializer.fromJson(inputStreamReader, RegisterRequest.class);

        return null;
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
