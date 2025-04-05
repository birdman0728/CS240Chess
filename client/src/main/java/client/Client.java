package client;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import Server.ServerFacade;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import requestsandresults.*;
import ui.EscapeSequences;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

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
//                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }



//    private String observe(String[] params) throws Exception{
//        if(signedIn){
//            //TODO figure out observe
//        }
//    }
    private String create(String[] params) throws Exception{ //TODO res is null?
        if(signedIn && params.length == 1){
            CreateResult res = server.create(new CreateRequest(AuthToken.authToken(), params[0]));
            return "Game " + params[0] + " created with id: " + res.gameID() + "\n";
        }else{
            throw new Exception("Not signed in or no name added");
        }
    }

    private String list() throws Exception{
        if(signedIn){
            ListResult res = server.listGames(new ListRequest(AuthToken.authToken()));
            if(res != null) {
                String printList = "";
                for(GameData game : res.games()){
                    printList += game.gameID() + " " + game.gameName() + " " + "White: " + game.whiteUsername() + " Black: " + game.blackUsername() + "\n";
                }

                return printList;
            }else{
                return "No games exist\n";
            }
        }else{
            throw new Exception("Not logged in\n");
        }
    }

    private String play(String[] params) throws Exception{
        if(signedIn){
            ChessGame.TeamColor color;
            if(Objects.equals(params[0], "White")){
                color = ChessGame.TeamColor.WHITE;
            }else if(Objects.equals(params[0], "Black")){
                color = ChessGame.TeamColor.BLACK;
            }else{
                throw new Exception("Please specify which team you'd like to join");
            }

            int gameID = Integer.parseInt(params[1]);
            StringBuilder output = new StringBuilder();
            ChessGame curGame = null;


            server.join(new JoinRequest(AuthToken.authToken(), color, gameID, AuthToken.username()));
            for(GameData game : server.listGames(new ListRequest(AuthToken.authToken())).games()){
                if(game.gameID() == gameID){
                    curGame = game.game();
                    output.append("Joinging Game: ").append(game.gameName()).append("\n\n");
                }
            }


            for(int i = 1; i < 9; i++){
                for(int j = 1; j < 9; j++ ){
                    assert curGame != null;
                    ChessPiece.PieceType type = curGame.getBoard().getPiece(new ChessPosition(j,i)).getPieceType();
                    output.append(CalcPiece(type, color));
                }
            }

            return output.toString();

        }else{
            throw new Exception("Please specify which game you'd like to join");
        }
    }

    private String CalcPiece(ChessPiece.PieceType type, ChessGame.TeamColor color){
        switch(type){
            case KING:
                if(color == ChessGame.TeamColor.BLACK){
                    return EscapeSequences.BLACK_KING;
                }else{
                    return EscapeSequences.WHITE_KING;
                }
                break;

            case QUEEN:
                if(color == ChessGame.TeamColor.BLACK){
                    return EscapeSequences.BLACK_QUEEN;
                }else{
                    return EscapeSequences.WHITE_QUEEN;
                }
                break;

            case BISHOP:
                if(color == ChessGame.TeamColor.BLACK){
                    return EscapeSequences.BLACK_BISHOP;
                }else{
                    return EscapeSequences.WHITE_BISHOP;
                }
                break;

            case ROOK:
                if(color == ChessGame.TeamColor.BLACK){
                    return EscapeSequences.BLACK_ROOK;
                }else{
                    return EscapeSequences.WHITE_ROOK;
                }
                break;

            case KNIGHT:
                if(color == ChessGame.TeamColor.BLACK){
                    return EscapeSequences.BLACK_KNIGHT;
                }else{
                    return EscapeSequences.WHITE_KNIGHT;
                }
                break;

            case PAWN:
                if(color == ChessGame.TeamColor.BLACK){
                    return EscapeSequences.BLACK_PAWN;
                }else{
                    return EscapeSequences.WHITE_PAWN;
                }
                break;

            default:
                return EscapeSequences.EMPTY;

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
            AuthToken = new AuthData(req.authToken(), req.username());
            signedIn = true;
            return "Successfully registered. \n";
        }else{
            throw new Exception("Please input a username password than email");
        }
    }

    private String login(String[] params) throws Exception {
        if(params.length == 2 && !signedIn){
            LoginResult req = server.login(new LoginRequest(params[0], params[1]));
            AuthToken = new AuthData(req.authToken(), req.username());
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