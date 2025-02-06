package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    private ChessPosition moveUp(ChessPosition ogMove, ChessBoard board){
        if(ogMove.getRow() < 8 &&
                (board.getPiece(new ChessPosition(ogMove.getRow() + 1, ogMove.getColumn())) == null ||
                board.getPiece(new ChessPosition(ogMove.getRow() + 1, ogMove.getColumn())).getTeamColor() != pieceColor)) {
            return new ChessPosition(ogMove.getRow() + 1, ogMove.getColumn());
        }
        return null;
    }
    private ChessPosition moveDown(ChessPosition ogMove, ChessBoard board){
        if(ogMove.getRow() > 1 &&
                (board.getPiece(new ChessPosition(ogMove.getRow() - 1, ogMove.getColumn())) == null ||
                board.getPiece(new ChessPosition(ogMove.getRow() - 1, ogMove.getColumn())).getTeamColor() != pieceColor)) {
            return new ChessPosition(ogMove.getRow() - 1, ogMove.getColumn());
        }
        return null;
    }

    private ChessPosition moveRight(ChessPosition ogMove, ChessBoard board){
        if(ogMove.getColumn() < 8 &&
                (board.getPiece(new ChessPosition(ogMove.getRow(), ogMove.getColumn() + 1)) == null ||
                board.getPiece(new ChessPosition(ogMove.getRow(), ogMove.getColumn() + 1)).getTeamColor() != pieceColor)){
            return new ChessPosition(ogMove.getRow(), ogMove.getColumn() + 1);
        }
        return null;
    }

    private ChessPosition moveLeft(ChessPosition ogMove, ChessBoard board){
        if(ogMove.getColumn() > 1 &&
                (board.getPiece(new ChessPosition(ogMove.getRow(), ogMove.getColumn() - 1)) == null ||
                board.getPiece(new ChessPosition(ogMove.getRow(), ogMove.getColumn() - 1)).getTeamColor() != pieceColor)){
            return new ChessPosition(ogMove.getRow(), ogMove.getColumn() - 1);
        }
        return null;
    }

    private ChessPosition moveUpRight(ChessPosition ogMove, ChessBoard board){
        if(ogMove.getColumn() < 8 && ogMove.getRow() < 8 &&
                (board.getPiece(new ChessPosition(ogMove.getRow() + 1, ogMove.getColumn() + 1)) == null ||
                board.getPiece(new ChessPosition(ogMove.getRow() + 1, ogMove.getColumn() + 1)).getTeamColor() != pieceColor)){
            return new ChessPosition(ogMove.getRow() + 1, ogMove.getColumn() + 1);
        }
        return null;
    }

    private ChessPosition moveUpLeft(ChessPosition ogMove, ChessBoard board){
        if(ogMove.getColumn() < 8 && ogMove.getRow() > 1 &&
                (board.getPiece(new ChessPosition(ogMove.getRow() + 1, ogMove.getColumn() - 1)) == null ||
                board.getPiece(new ChessPosition(ogMove.getRow() + 1, ogMove.getColumn() - 1)).getTeamColor() != pieceColor)){
            return new ChessPosition(ogMove.getRow() + 1, ogMove.getColumn() - 1);
        }
        return null;
    }

    private ChessPosition moveDownRight(ChessPosition ogMove, ChessBoard board){
        if(ogMove.getColumn() > 1 && ogMove.getRow() < 8 &&
                (board.getPiece(new ChessPosition(ogMove.getRow() - 1, ogMove.getColumn() + 1)) == null ||
                board.getPiece(new ChessPosition(ogMove.getRow() - 1, ogMove.getColumn() + 1)).getTeamColor() != pieceColor)){
            return new ChessPosition(ogMove.getRow() - 1, ogMove.getColumn() + 1);
        }
        return null;
    }

    private ChessPosition moveDownLeft(ChessPosition ogMove, ChessBoard board){
        if(ogMove.getColumn() > 1 && ogMove.getRow() > 1 &&
                (board.getPiece(new ChessPosition(ogMove.getRow() - 1, ogMove.getColumn() - 1)) == null ||
                board.getPiece(new ChessPosition(ogMove.getRow() - 1, ogMove.getColumn() - 1)).getTeamColor() != pieceColor)){
            return new ChessPosition(ogMove.getRow() - 1, ogMove.getColumn() - 1);
        }
        return null;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection <ChessMove> validMoves = new HashSet<>();

        int y = myPosition.getRow();
        int x = myPosition.getColumn();

        switch(type) {
            //I'll need to add this to a subclass?
            case KING:

                //up-right
                if(y+1<= 8 && x+1 <= 8){
                    if (board.getPiece(new ChessPosition(y + 1, x + 1)) == null || board.getPiece(new ChessPosition(y + 1, x + 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y+ 1, x +1), null));
                    }
                }

                //down-left
                if(y-1>=1 && x-1>=1) {
                    if (board.getPiece(new ChessPosition(y - 1, x - 1)) == null || board.getPiece(new ChessPosition(y - 1, x - 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x - 1), null));
                    }
                }

                //down-right
                if(y-1>=1 && x+1<=8) {
                    if (board.getPiece(new ChessPosition(y - 1, x + 1)) == null || board.getPiece(new ChessPosition(y - 1, x + 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), null));
                    }
                }

                //up-left
                if(y+1 <=8 && x-1>=1) {
                    if (board.getPiece(new ChessPosition(y + 1, x - 1)) == null || board.getPiece(new ChessPosition(y + 1, x - 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x - 1), null));
                    }
                }

                //up
                if(y+1<=8) {
                    if (board.getPiece(new ChessPosition(y + 1, myPosition.getColumn())) == null || board.getPiece(new ChessPosition(y + 1, x)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, myPosition.getColumn()), null));
                    }
                }

                //down
                if(y-1>=1) {
                    if (board.getPiece(new ChessPosition(y - 1, myPosition.getColumn())) == null || board.getPiece(new ChessPosition(y - 1, x)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, myPosition.getColumn()), null));
                    }
                }

                //right
                if(x+1<=8) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), x + 1)) == null || board.getPiece(new ChessPosition(y, x + 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), x + 1), null));
                    }
                }

                //left
                if(x-1>=1) {
                    if (board.getPiece(new ChessPosition(myPosition.getRow(), x - 1)) == null || board.getPiece(new ChessPosition(y, x - 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), x - 1), null));
                    }
                }

                //check stuff???
                break;


            case QUEEN:
                //up-right
                for(int i = myPosition.getRow(), j = myPosition.getColumn(); j < 8 && i < 8;j++, i++){
                    if (board.getPiece(new ChessPosition(i + 1, j +1)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j +1), null));
                    } else if (board.getPiece(new ChessPosition(i + 1, j +1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j +1), null));
                        break;
                    } else {
                        break;
                    }
                }
                //down-left
                for(int i = myPosition.getRow(), j = myPosition.getColumn(); i > 1 && j > 1;j--, i--){
                    if (board.getPiece(new ChessPosition(i - 1, j - 1)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j -1), null));
                    } else if (board.getPiece(new ChessPosition(i - 1, j -1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j -1), null));
                        break;
                    } else {
                        break;
                    }
                }

                //down-right
                for(int i = myPosition.getRow(), j = myPosition.getColumn(); i > 1 && j < 8 ; j++, i--){
                    if (board.getPiece(new ChessPosition(i - 1, j +1)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j +1), null));
                    } else if (board.getPiece(new ChessPosition(i - 1, j +1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j +1), null));
                        break;
                    } else {
                        break;
                    }
                }
                //up-left
                for(int i = myPosition.getRow(), j = myPosition.getColumn(); j > 1 && i < 8; j--, i++){
                    if (board.getPiece(new ChessPosition(i + 1, j - 1)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j -1), null));
                    } else if (board.getPiece(new ChessPosition(i + 1, j -1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j -1), null));
                        break;
                    } else {
                        break;
                    }
                }
                //up
                for(int i = myPosition.getRow(); i < 8; i++){
                    if(board.getPiece(new ChessPosition(i + 1, myPosition.getColumn())) == null){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, myPosition.getColumn()), null));
                    }else if(board.getPiece(new ChessPosition(i + 1, myPosition.getColumn())).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, myPosition.getColumn()), null));
                        break;
                    }else {
                        break;
                    }
                }

                //down
                for(int i = myPosition.getRow(); i > 1; i--){
                    if(board.getPiece(new ChessPosition(i - 1, myPosition.getColumn())) == null){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, myPosition.getColumn()), null));
                    }else if(board.getPiece(new ChessPosition(i - 1, myPosition.getColumn())).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, myPosition.getColumn()), null));
                        break;
                    }else {
                        break;
                    }
                }
                //right
                for(int i = myPosition.getColumn(); i < 8; i++){
                    if(board.getPiece(new ChessPosition(myPosition.getRow(), i + 1)) == null){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i + 1), null));
                    }else if(board.getPiece(new ChessPosition(myPosition.getRow(), i + 1)).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i + 1), null));
                        break;
                    }else {
                        break;
                    }
                }

                //left
                for(int i = myPosition.getColumn(); i > 1; i--){
                    if(board.getPiece(new ChessPosition(myPosition.getRow(), i - 1)) == null){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i - 1), null));
                    }else if(board.getPiece(new ChessPosition(myPosition.getRow(), i - 1)).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i - 1), null));
                        break;
                    }else {
                        break;
                    }
                }

                break;


            case BISHOP:
                //up-right
                for(int i = myPosition.getRow(), j = myPosition.getColumn(); j < 8 && i < 8;j++, i++){
                    if (board.getPiece(new ChessPosition(i + 1, j +1)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j +1), null));
                    } else if (board.getPiece(new ChessPosition(i + 1, j +1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j +1), null));
                        break;
                    } else {
                        break;
                    }
                }
                //down-left
                for(int i = myPosition.getRow(), j = myPosition.getColumn(); i > 1 && j > 1;j--, i--){
                    if (board.getPiece(new ChessPosition(i - 1, j - 1)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j -1), null));
                    } else if (board.getPiece(new ChessPosition(i - 1, j -1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j -1), null));
                        break;
                    } else {
                        break;
                    }
                }

                //down-right
                for(int i = myPosition.getRow(), j = myPosition.getColumn(); i > 1 && j < 8 ; j++, i--){
                    if (board.getPiece(new ChessPosition(i - 1, j +1)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j +1), null));
                    } else if (board.getPiece(new ChessPosition(i - 1, j +1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j +1), null));
                        break;
                    } else {
                        break;
                    }
                }
                //up-left
                for(int i = myPosition.getRow(), j = myPosition.getColumn(); j > 1 && i < 8; j--, i++){
                    if (board.getPiece(new ChessPosition(i + 1, j - 1)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j -1), null));
                    } else if (board.getPiece(new ChessPosition(i + 1, j -1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j -1), null));
                        break;
                    } else {
                        break;
                    }
                }


                break;


            case KNIGHT:
                y = myPosition.getRow();
                x = myPosition.getColumn();


                if(y + 2  <= 8 && x + 1  <= 8) {
                    if (board.getPiece(new ChessPosition(y + 2, x + 1)) == null || board.getPiece(new ChessPosition(y + 2, x + 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 2, x + 1), null));
                    }
                }

                if(y + 2  <= 8 && x - 1 >= 1) {
                    if (board.getPiece(new ChessPosition(y + 2, x - 1)) == null || board.getPiece(new ChessPosition(y + 2, x - 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 2, x - 1), null));
                    }
                }

                if(y + 1  <= 8 && x - 2 >= 1) {
                    if (board.getPiece(new ChessPosition(y + 1, x - 2)) == null || board.getPiece(new ChessPosition(y + 1, x - 2)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x - 2), null));
                    }
                }

                if(y - 1 >= 1 && x - 2 >= 1) {
                    if (board.getPiece(new ChessPosition(y - 1, x - 2)) == null || board.getPiece(new ChessPosition(y - 1, x - 2)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x - 2), null));
                    }
                }

                if(y - 2 >= 1 && x - 1 >= 1) {
                    if (board.getPiece(new ChessPosition(y - 2, x - 1)) == null || board.getPiece(new ChessPosition(y - 2, x - 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 2, x - 1), null));
                    }
                }

                if(y - 2 >= 1 && x + 1  <= 8) {
                    if (board.getPiece(new ChessPosition(y - 2, x + 1)) == null || board.getPiece(new ChessPosition(y - 2, x + 1)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 2, x + 1), null));
                    }
                }

                if(y - 1 >= 1 && x + 2  <= 8) {
                    if (board.getPiece(new ChessPosition(y - 1, x + 2)) == null || board.getPiece(new ChessPosition(y - 1, x + 2)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 2), null));
                    }
                }

                if(y + 1  <= 8 && x + 2  <= 8) {
                    if (board.getPiece(new ChessPosition(y + 1, x + 2)) == null || board.getPiece(new ChessPosition(y + 1, x + 2)).getTeamColor() != pieceColor) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x + 2), null));
                    }
                }

                break;


            case ROOK:
                //up
                for(int i = myPosition.getRow(); i < 8; i++){
                    if(board.getPiece(new ChessPosition(i + 1, myPosition.getColumn())) == null){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, myPosition.getColumn()), null));
                    }else if(board.getPiece(new ChessPosition(i + 1, myPosition.getColumn())).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, myPosition.getColumn()), null));
                        break;
                    }else {
                        break;
                    }
                }

                //down
                for(int i = myPosition.getRow(); i > 1; i--){
                    if(board.getPiece(new ChessPosition(i - 1, myPosition.getColumn())) == null){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, myPosition.getColumn()), null));
                    }else if(board.getPiece(new ChessPosition(i - 1, myPosition.getColumn())).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, myPosition.getColumn()), null));
                        break;
                    }else {
                        break;
                    }
                }
                //right
                for(int i = myPosition.getColumn(); i < 8; i++){
                    if(board.getPiece(new ChessPosition(myPosition.getRow(), i + 1)) == null){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i + 1), null));
                    }else if(board.getPiece(new ChessPosition(myPosition.getRow(), i + 1)).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i + 1), null));
                        break;
                    }else {
                        break;
                    }
                }

                //left
                for(int i = myPosition.getColumn(); i > 1; i--){
                    if(board.getPiece(new ChessPosition(myPosition.getRow(), i - 1)) == null){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i - 1), null));
                    }else if(board.getPiece(new ChessPosition(myPosition.getRow(), i - 1)).getTeamColor() != pieceColor){
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i - 1), null));
                        break;
                    }else {
                        break;
                    }
                }
                break;


            case PAWN:
                y = myPosition.getRow();
                x = myPosition.getColumn();

                ////white
                if(pieceColor == ChessGame.TeamColor.WHITE) {
                    //normal forward
                    if (y + 1 < 8) {
                        if (board.getPiece(new ChessPosition(y + 1, x)) == null) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), null));
                        }
                    } else if(y+1 == 8){
                        if (board.getPiece(new ChessPosition(y + 1, x)) == null) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), PieceType.QUEEN ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), PieceType.ROOK ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), PieceType.BISHOP ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), PieceType.KNIGHT ));
                        }
                    }
                    //two move
                    if (y == 2 && board.getPiece(new ChessPosition(y + 2, x)) == null && board.getPiece(new ChessPosition(y + 1, x)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 2, x), null));
                    }
                    //capture right
                    if (y + 1 < 8 && x + 1 < 8) {
                        if (board.getPiece(new ChessPosition(y + 1, x + 1)) != null) {
                            if (board.getPiece(new ChessPosition(y + 1, x + 1)).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x + 1), null));
                            }
                        }
                    }else if(y+1 == 8 && x + 1 < 8){
                        if (board.getPiece(new ChessPosition(y + 1, x+1)) != null) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x+1), PieceType.QUEEN ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x+1), PieceType.ROOK ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x+1), PieceType.BISHOP ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x+1), PieceType.KNIGHT ));
                        }
                    }
                    //capture left
                    if (y + 1 < 8 && x - 1 > 1) {
                        if (board.getPiece(new ChessPosition(y + 1, x - 1)) != null) {
                            if (board.getPiece(new ChessPosition(y + 1, x - 1)).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x - 1), null));
                            }
                        }
                    }else if(y+1 == 8 && x - 1 > 1){
                        if (board.getPiece(new ChessPosition(y + 1, x-1)) != null) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x-1), PieceType.QUEEN ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x-1), PieceType.ROOK ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x-1), PieceType.BISHOP ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x-1), PieceType.KNIGHT ));
                        }
                    }
                }

                //promotion


                ////black
                if(pieceColor == ChessGame.TeamColor.BLACK) {
                    //normal forward
                    if (y - 1 > 1) {
                        if (board.getPiece(new ChessPosition(y - 1, x)) == null) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), null));
                        }
                    } else if(y-1 == 1){
                        if (board.getPiece(new ChessPosition(y - 1, x)) == null) {
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), PieceType.QUEEN ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), PieceType.ROOK ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), PieceType.BISHOP ));
                            validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), PieceType.KNIGHT ));
                        }
                    }

                    //two move
                    if (y == 7 && board.getPiece(new ChessPosition(y - 2, x)) == null && board.getPiece(new ChessPosition(y - 1, x)) == null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 2, x), null));
                    }

                    //capture right
                    if (y - 1 > 1 && x + 1 < 8) {
                        if (board.getPiece(new ChessPosition(y - 1, x + 1)) != null) {
                            if (board.getPiece(new ChessPosition(y - 1, x + 1)).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), null));
                            }
                        }
                    }else if(y-1 == 1 && x + 1 <= 8){
                        if (board.getPiece(new ChessPosition(y - 1, x + 1)) != null) {
                            if (board.getPiece(new ChessPosition(y - 1, x + 1)).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), PieceType.QUEEN ));
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), PieceType.ROOK ));
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), PieceType.BISHOP ));
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), PieceType.KNIGHT ));
                            }
                        }
                    }
                    //capture left
                    if (y - 1 > 1 && x - 1 > 1) {
                        if (board.getPiece(new ChessPosition(y - 1, x - 1)) != null) {
                            if (board.getPiece(new ChessPosition(y - 1, x - 1)).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x - 1), null));
                            }
                        }
                    }else if(y-1 == 1 && x - 1 >= 1){
                        if (board.getPiece(new ChessPosition(y - 1, x - 1)) != null) {
                            if (board.getPiece(new ChessPosition(y - 1, x - 1)).getTeamColor() != pieceColor) {
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x-1), PieceType.QUEEN ));
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x-1), PieceType.ROOK ));
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x-1), PieceType.BISHOP ));
                                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x-1), PieceType.KNIGHT ));
                            }
                        }
                    }
                }

                //en passant white
                //en passant black

                break;

        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
