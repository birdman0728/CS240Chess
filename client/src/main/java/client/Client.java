package client;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import Server.ServerFacade;
import chess.ChessGame;
import model.AuthData;
import requestsandresults.*;

import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private final ServerFacade server;
    boolean signedIn;
    AuthData AuthToken;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("♕ 240 Chess Client");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public String help(){
        if(signedIn){
            return """
                    *Create Game: create <Game Name>
                    *List Games: list
                    *Play Game: play <ID> <White or Black>
                    *Observe Game: observe <ID>
                    *Logout: logout
                    *Help: help
                    """;
        }else{
            return """
                    *Register: register <Username> <Password> <Email>
                    *Login: login <Username> <Password>
                    *Help: help
                    *Quit: quit
                    """;
        }
    }

    private String eval(String input){
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params); //TODO implement an actual exception in server
                case "login" -> login(params);

                case "create" -> create(params);
                case "list" -> list();
                case "play" -> play(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String create(String[] params) throws Exception{}
    private String list() throws Exception{}

    private String play(String[] params) throws Exception{
        if(signedIn){
            ChessGame.TeamColor color;
            if(params[0] == "White"){
                color = ChessGame.TeamColor.WHITE;
            }else if(params[0] == "Black"){
                color = ChessGame.TeamColor.BLACK;
            }else{
                throw new Exception("Please specify which team you'd like to join");
            }
            int gameID = Integer.parseInt(params[1]);

            server.join(new JoinRequest(AuthToken.authToken(), color, gameID, AuthToken.username()));
            return "Joinging Game" + gameID;
        }else{
            throw new Exception("Please specify which game you'd like to join");
        }
    }

    private String observe(String[] params) throws Exception{
        if(signedIn){
            //TODO figure out observe
        }
    }

    private String logout() throws Exception{
        if(signedIn){
            server.logout(new LogoutRequest(AuthToken.authToken()));
            signedIn = false;
            return "Logged out. \n";
        }else{
            throw new Exception("Not signed in");
        }
    }


    private String register(String[] params) throws Exception {
        if(params.length == 3 && !signedIn){
            RegisterResult req = server.register(new RegisterRequest(params[0], params[1], params[2]));
            AuthToken = new AuthData(req.username(), req.authToken());
            signedIn = true;
            return "Successfully registered. \n";
        }else{
            throw new Exception("Please input a username password than email");
        }
    }

    private String login(String[] params) throws Exception {
        if(params.length == 2 && !signedIn){
            LoginResult req = server.login(new LoginRequest(params[0], params[1]));
            AuthToken = new AuthData(req.username(), req.authToken());
            signedIn = true;
            return "Successfully signed in. \n";
        }else{
            throw new Exception("Please put email and password");
        }
    }

//    public void notify(Notification notification) {
//        System.out.println(RED + notification.message());
//        printPrompt();
//    }

    private void printPrompt() {
//        System.out.print("\n" + RESET + ">>> " + GREEN);
    }

}