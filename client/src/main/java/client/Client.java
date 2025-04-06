package client;

//import client.websocket.NotificationHandler;
//import webSocketMessages.Notification;

import Server.ServerFacade;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.sun.source.tree.WhileLoopTree;
import model.AuthData;
import model.GameData;
import requestsandresults.*;
import ui.EscapeSequences;

import java.util.*;

public class Client {
    private ServerFacade server;
    boolean signedIn;
    AuthData AuthToken;
    List<GameData> gamesList;

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
            if(!signedIn) {
                return switch (cmd) {
                    case "register" -> register(params); //TODO implement an actual exception in server
                    case "login" -> login(params);
                    default -> help();
                };
            }else {
                return switch (cmd) {
                    case "create" -> create(params);
                    case "list" -> list();
                    case "play" -> play(params);
//                case "observe" -> observe(params);
                    case "logout" -> logout();
                    case "quit" -> "quit";
                    default -> help();
                };
            }
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }



//    private String observe(String[] params) throws Exception{
//        if(signedIn){
//            //TODO figure out observe/do redraw on it's own
//              TODO Make errors not show up
//              TODO make Server Facade throw errors and tests adjust
//        }
//    }

    private String create(String[] params) throws Exception{
        if(signedIn && params.length == 1){
            CreateResult res = server.create(new CreateRequest(AuthToken.authToken(), params[0]));
            return "Game " + params[0] + " created with id: " + res.gameID() + "\n";
        }else{
            throw new Exception("Not signed in or no name added\n");
        }
    }

    private String list() throws Exception{//TODO Make number independent of gameID
        if(signedIn){
            ListResult res = server.listGames(new ListRequest(AuthToken.authToken()));
            if(res != null) {
                String printList = "";
                int i = 1;
                for(GameData game : res.games()){
                    printList += i + " " + game.gameName() + " " + "White: " + game.whiteUsername() + " Black: " + game.blackUsername() + "\n";
                    gamesList.add(game);
                    i++;
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
            if(Objects.equals(params[1], "white")){
                color = ChessGame.TeamColor.WHITE;
            }else if(Objects.equals(params[1], "black")){
                color = ChessGame.TeamColor.BLACK;
            }else{
                throw new Exception("Please specify which team you'd like to join\n");
            }

            int gameID = Integer.parseInt(params[0]);
            StringBuilder output = new StringBuilder();
            ChessGame curGame = null;


            server.join(new JoinRequest(AuthToken.authToken(), color, gameID, AuthToken.username()));
            for(GameData game : server.listGames(new ListRequest(AuthToken.authToken())).games()){
                if(game.gameID() == gameID){
                    curGame = game.game();
                    output.append("Joining Game: ").append(game.gameName()).append("\n\n");//TODO specify the order
                }
            }

            output.append(drawBoard(color, curGame));

            return output.toString();

        }else{
            throw new Exception("Please specify which game you'd like to join\n");
        }
    }

    private String boardFormat(ChessGame.TeamColor color){
        if (color == ChessGame.TeamColor.WHITE){
            return "8\n7\n6\n5\n4\n3\n2\n1\n  a b c d e f g h\n\n";
        }else{
            return "1\n2\n3\n4\n5\n6\n7\n8\n  h g f e d c b a\n\n";
        }
    }

    private String drawBoard(ChessGame.TeamColor color, ChessGame curGame){
        StringBuilder output = new StringBuilder();
        boolean whiteBG = false;
        output.append("Board format\n");
        if(color == ChessGame.TeamColor.WHITE){
            output.append(boardFormat(color));
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    if(whiteBG){
                        output.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                        whiteBG = false;
                    }else{
                        output.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        whiteBG = true;
                    }

                    assert curGame != null;
                    if(curGame.getBoard().getPiece(new ChessPosition(i, j)) != null) {
                        ChessPiece.PieceType type = curGame.getBoard().getPiece(new ChessPosition(i, j)).getPieceType();
                        output.append(CalcPiece(type, color)).append(EscapeSequences.RESET_TEXT_COLOR);
                        output.append(EscapeSequences.RESET_BG_COLOR);
                    }else{
                        output.append(EscapeSequences.EMPTY).append(EscapeSequences.RESET_BG_COLOR);
                    }
                }
                output.append("\n");
                whiteBG = !whiteBG;
            }
        }else{
            output.append(boardFormat(color));
            for (int i = 8; i > 0; i--) {
                for (int j = 8; j > 0; j--) {
                    if(whiteBG){
                        output.append(EscapeSequences.SET_BG_COLOR_BLACK);
                        whiteBG = false;
                    }else{
                        output.append(EscapeSequences.SET_BG_COLOR_WHITE);
                        whiteBG = true;
                    }

                    assert curGame != null;
                    if(curGame.getBoard().getPiece(new ChessPosition(i, j)) != null) {
                        ChessPiece.PieceType type = curGame.getBoard().getPiece(new ChessPosition(i, j)).getPieceType();
                        output.append(CalcPiece(type, color)).append(EscapeSequences.RESET_TEXT_COLOR);
                        output.append(EscapeSequences.RESET_BG_COLOR);
                    }else{
                        output.append(EscapeSequences.EMPTY).append(EscapeSequences.RESET_BG_COLOR);
                    }
                }
                output.append("\n");
                whiteBG = !whiteBG;
            }
        }
        return output.toString();
    }

    private String CalcPiece(ChessPiece.PieceType type, ChessGame.TeamColor color) {
        switch (type) {
            case KING:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_KING + EscapeSequences.SET_TEXT_COLOR_BLUE;
                } else {
                    return EscapeSequences.WHITE_KING + EscapeSequences.SET_TEXT_COLOR_RED;
                }

            case QUEEN:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_QUEEN + EscapeSequences.SET_TEXT_COLOR_BLUE;
                } else {
                    return EscapeSequences.WHITE_QUEEN + EscapeSequences.SET_TEXT_COLOR_RED;
                }

            case BISHOP:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_BISHOP + EscapeSequences.SET_TEXT_COLOR_BLUE;
                } else {
                    return EscapeSequences.WHITE_BISHOP + EscapeSequences.SET_TEXT_COLOR_RED;
                }

            case ROOK:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_ROOK + EscapeSequences.SET_TEXT_COLOR_BLUE;
                } else {
                    return EscapeSequences.WHITE_ROOK + EscapeSequences.SET_TEXT_COLOR_RED;
                }

            case KNIGHT:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_KNIGHT + EscapeSequences.SET_TEXT_COLOR_BLUE;
                } else {
                    return EscapeSequences.WHITE_KNIGHT + EscapeSequences.SET_TEXT_COLOR_RED;
                }

            case PAWN:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_PAWN + EscapeSequences.SET_TEXT_COLOR_BLUE;
                } else {
                    return EscapeSequences.WHITE_PAWN + EscapeSequences.SET_TEXT_COLOR_RED;
                }

            default:
                return EscapeSequences.EMPTY;

        }
    }

    private int calcLetter(String letter){
        switch(letter) {
            case "a":
                return 1;
            case "b":
                return 2;
            case "c":
                return 3;
            case "d":
                return 4;
            case "e":
                return 5;
            case "f":
                return 6;
            case "g":
                return 7;
            case "h":
                return 8;
            default: return 0;
        }
    }

    private String logout() throws Exception{
        if(signedIn){
            server.logout(new LogoutRequest(AuthToken.authToken()));
            signedIn = false;
            return "Logged out. \n";
        }else{
            throw new Exception("Not signed in\n");
        }
    }


    private String register(String[] params) throws Exception {
        if(params.length == 3 && !signedIn){
            RegisterResult req = server.register(new RegisterRequest(params[0], params[1], params[2]));
            AuthToken = new AuthData(req.authToken(), req.username());
            signedIn = true;
            return "Successfully registered. \n";
        }else{
            throw new Exception("Please input a username password than email\n");
        }
    }

    private String login(String[] params) throws Exception {
        if(params.length == 2 && !signedIn){
            LoginResult req = server.login(new LoginRequest(params[0], params[1]));
            AuthToken = new AuthData(req.authToken(), req.username());
            signedIn = true;
            return "Successfully signed in. \n";
        }else{
            throw new Exception("Please put email and password\n");
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