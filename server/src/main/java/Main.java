import chess.*;
import dataaccess.DataAccessException;
import server.Server;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess server.Server: " + piece);
        int inputPort = 8080;

        if(0 < args.length){
            try{
                inputPort = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.out.println("Improper port number");
            }
        }

        Server server = new Server();
        server.run(inputPort);
    }
}