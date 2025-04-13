package client;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.ResponseException;
import ui.EscapeSequences;

public class InGameCommands {
    //TODO: make notification for each one of these classes
    ChessGame.TeamColor color;
    ChessGame game;
    boolean inGame = true;
    public InGameCommands(ChessGame.TeamColor color, ChessGame game){
        //server probably as well as game
        this.color = color;
        this.game = game;
    }

    public boolean inGame(){
        return inGame;
    }

    public String help(){
        return """
                *Help: help
                *Redraw Chess Board: redraw
                *Leave: leave
                *Make Move: move <starting position> <ending position>
                *Resign: resign
                *Highlight Legal Moves: highlight <position of piece you want to highlight>
                """;
    }
    public String drawBoard(){
        StringBuilder output = new StringBuilder();
        boolean whiteBG = false;
        if(color == ChessGame.TeamColor.WHITE){
            for (int i = 8; i > 0; i--) {
                output.append(i).append(" ");
                for (int j = 1; j < 9; j++) {
                    if(whiteBG){
                        output.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                        whiteBG = false;
                    }else{
                        output.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        whiteBG = true;
                    }

                    assert game != null;
                    if(game.getBoard().getPiece(new ChessPosition(i, j)) != null) {
                        ChessPiece.PieceType type = game.getBoard().getPiece(new ChessPosition(i, j)).getPieceType();
                        output.append(calcPiece(type, game.getBoard().getPiece(new ChessPosition(i,j)).getTeamColor()));
                        output.append(EscapeSequences.RESET_TEXT_COLOR).append(EscapeSequences.RESET_BG_COLOR);
                    }else{
                        output.append(EscapeSequences.EMPTY).append(EscapeSequences.RESET_BG_COLOR);
                    }
                }
                output.append("\n");
                whiteBG = !whiteBG;
            }
            output.append("   a   b  c   d  e   f   g   h");
        }else{
            for (int i = 1; i < 9; i++) {
                output.append(i).append(" ");
                for (int j = 8; j > 0; j--) {
                    if(whiteBG){
                        output.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                        whiteBG = false;
                    }else{
                        output.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                        whiteBG = true;
                    }

                    assert game != null;
                    if(game.getBoard().getPiece(new ChessPosition(i, j)) != null) {
                        ChessPiece.PieceType type = game.getBoard().getPiece(new ChessPosition(i, j)).getPieceType();
                        output.append(calcPiece(type, game.getBoard().getPiece(new ChessPosition(i,j)).getTeamColor()));
                        output.append(EscapeSequences.RESET_BG_COLOR);
                    }else{
                        output.append(EscapeSequences.EMPTY).append(EscapeSequences.RESET_BG_COLOR);
                    }
                }
                output.append("\n");
                whiteBG = !whiteBG;
            }
            output.append("   h   g  f   e   d  c   b   a");
        }
        return output.toString();
    }
    private String calcPiece(ChessPiece.PieceType type, ChessGame.TeamColor color) {
        switch (type) {
            case KING:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_KING;
                } else {
                    return EscapeSequences.WHITE_KING;
                }

            case QUEEN:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_QUEEN;
                } else {
                    return EscapeSequences.WHITE_QUEEN;
                }

            case BISHOP:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_BISHOP;
                } else {
                    return EscapeSequences.WHITE_BISHOP;
                }

            case ROOK:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_ROOK;
                } else {
                    return EscapeSequences.WHITE_ROOK;
                }

            case KNIGHT:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_KNIGHT;
                } else {
                    return EscapeSequences.WHITE_KNIGHT;
                }

            case PAWN:
                if (color == ChessGame.TeamColor.BLACK) {
                    return EscapeSequences.BLACK_PAWN;
                } else {
                    return EscapeSequences.WHITE_PAWN;
                }

            default:
                return EscapeSequences.EMPTY;

        }
    }


    public String leave(){
        inGame = false;
        return null;
    }
    public String makeMove(String startMove, String endMove){
        return null;
    }
    private int calcLetter(String letter) throws ResponseException {
        return switch (letter) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new ResponseException("Please put in a proper input", 401);
        };
    }

    public String resign(){
        inGame = false;
        return null;
    }
    public String hightlight(String position){
        return null;
    }

}
