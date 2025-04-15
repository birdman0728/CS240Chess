package client;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import serverfacade.ServerFacade;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.ResponseException;
import requestsandresults.*;
import ui.EscapeSequences;

import java.util.*;

public class Client {
    private final ServerFacade server;
    boolean signedIn;
    AuthData authToken;
    List<GameData> gamesList = new ArrayList<>();
    InGameCommands IGC;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("♕ 240 Chess Client");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
//            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(result);
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
            if(!signedIn) {
                return switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            }else {
                if (IGC == null || !IGC.inGame()) {
                    return switch (cmd) {
                        case "create" -> create(params);
                        case "list" -> list();
                        case "play" -> play(params);
                        case "observe" -> observe(params);
                        case "logout" -> logout();
                        case "quit" -> "quit";
                        default -> help();
                    };
                } else {
                    return switch (cmd) {
                        case "redraw" -> IGC.drawBoard();
                        case "leave" -> IGC.leave();
                        case "move" -> IGC.makeMove(params);
                        case "resign" -> IGC.resign();
                        case "highlight" -> IGC.hightlight(params);
                        default -> IGC.help();
                    };
                }
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }catch (Exception ex){
            return "An Error Occurred: Please try again and double check your input";

        }
    }

    private String observe(String[] params) throws Exception{
        StringBuilder output = new StringBuilder();
        if(signedIn){
            gamesList.clear();
            gamesList.addAll(server.listGames(new ListRequest(authToken.authToken())).games());
            if(gamesList != null) {
                int gameID = Integer.parseInt(params[0]) - 1;
                ChessGame curGame = null;
                gameID = gamesList.get(gameID).gameID();

                for(GameData game : server.listGames(new ListRequest(authToken.authToken())).games()){
                    if(game.gameID() == gameID){
                        curGame = game.game();
                        output.append("Observing Game: ").append(game.gameName()).append("\n\n");
                    }
                }

                IGC = new InGameCommands(ChessGame.TeamColor.WHITE, curGame, true);
                output.append(IGC.drawBoard());

                return output.toString();
            }else{
                return "No games to observe";
            }
        }else{
            throw new ResponseException("You are not signed in", 401);
        }
    }

    private String create(String[] params) throws Exception{
        if(signedIn && params.length == 1){
            CreateResult res = server.create(new CreateRequest(authToken.authToken(), params[0]));
            return "Game " + params[0] + " created.";
        }else{
            throw new ResponseException("Not signed in or no name added", 401);
        }
    }

    private String list() throws Exception{
        if(signedIn){
            gamesList.clear();
            gamesList.addAll(server.listGames(new ListRequest(authToken.authToken())).games());
            if(gamesList != null) {
                StringBuilder printList = new StringBuilder();
                int i = 1;
                for(GameData game : gamesList){
                    printList.append(i).append(" | ").append(game.gameName()).append(" ").append("White: ")
                            .append(game.whiteUsername()).append(" | Black: ").append(game.blackUsername()).append("\n");
                    i++;
                }

                return printList.toString();
            }else{
                return "No games exis";
            }
        }else{
            throw new ResponseException("Not logged in", 401);
        }
    }

    private String play(String[] params) throws Exception{
        gamesList.clear();
        gamesList.addAll(server.listGames(new ListRequest(authToken.authToken())).games());

        if(signedIn){
            ChessGame.TeamColor color;
            if(Objects.equals(params[1], "white")){
                color = ChessGame.TeamColor.WHITE;
            }else if(Objects.equals(params[1], "black")){
                color = ChessGame.TeamColor.BLACK;
            }else{
                throw new ResponseException("Please specify which team you'd like to join", 400);
            }

            int gameID = Integer.parseInt(params[0]) - 1;
            StringBuilder output = new StringBuilder();
            ChessGame curGame = null;

            gameID = gamesList.get(gameID).gameID();


            server.join(new JoinRequest(authToken.authToken(), color, gameID, authToken.username()));
            for(GameData game : server.listGames(new ListRequest(authToken.authToken())).games()){
                if(game.gameID() == gameID){
                    curGame = game.game();
                    output.append("Joining Game: ").append(game.gameName()).append("\n\n");
                }
            }
            IGC = new InGameCommands(color, curGame, false);
            output.append(IGC.drawBoard());

            return output.toString();

        }else{
            throw new ResponseException("Please specify which game you'd like to join", 400);
        }
    }


    private String logout() throws Exception{
        if(signedIn){
            server.logout(new LogoutRequest(authToken.authToken()));
            signedIn = false;
            return "Logged out.";
        }else{
            throw new ResponseException("Not signed in", 401);
        }
    }


    private String register(String[] params) throws Exception {
        if(params.length == 3 && !signedIn){
            RegisterResult req = server.register(new RegisterRequest(params[0], params[1], params[2]));
            authToken = new AuthData(req.authToken(), req.username());
            signedIn = true;
            return "Successfully registered.";
        }else{
            throw new ResponseException("Please input a username password than email", 400);
        }
    }

    private String login(String[] params) throws Exception {
        if(params.length == 2 && !signedIn){
            LoginResult req = server.login(new LoginRequest(params[0], params[1]));
            authToken = new AuthData(req.authToken(), req.username());
            signedIn = true;
            return "Successfully signed in.";
        }else{
            throw new ResponseException("Please put email and password.", 400);
        }
    }
}