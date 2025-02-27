package server;

import spark.*;

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

    private Object Register(Request req, Response res){

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
