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

    //null check class
    private boolean checkNull(ChessBoard board, ChessPosition position){
        return board.getPiece(position) == null;
    }
    //get piece class
    private boolean checkPieceColor(ChessBoard board, ChessPosition position){
        return board.getPiece(position).getTeamColor() != pieceColor;
    }

    private Collection<ChessMove> rookMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves){
        //up
        for(int i = myPosition.getRow(); i < 8; i++){
            if(checkNull(board, new ChessPosition(i + 1, myPosition.getColumn()))){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, myPosition.getColumn()), null));
            }else if(checkPieceColor(board, new ChessPosition(i + 1, myPosition.getColumn()))){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, myPosition.getColumn()), null));
                break;
            }else {
                break;
            }
        }

        //down
        for(int i = myPosition.getRow(); i > 1; i--){
            if(checkNull(board, new ChessPosition(i - 1, myPosition.getColumn()))){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, myPosition.getColumn()), null));
            }else if(checkPieceColor(board, new ChessPosition(i - 1, myPosition.getColumn()))){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, myPosition.getColumn()), null));
                break;
            }else {
                break;
            }
        }
        //right
        for(int i = myPosition.getColumn(); i < 8; i++){
            if(checkNull(board, new ChessPosition(myPosition.getRow(), i + 1))){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i + 1), null));
            }else if(checkPieceColor(board, new ChessPosition(myPosition.getRow(), i + 1))){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i + 1), null));
                break;
            }else {
                break;
            }
        }

        //left
        for(int i = myPosition.getColumn(); i > 1; i--){
            if(checkNull(board, new ChessPosition(myPosition.getRow(), i - 1))){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i - 1), null));
            }else if(checkPieceColor(board, new ChessPosition(myPosition.getRow(), i - 1))){
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), i - 1), null));
                break;
            }else {
                break;
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> bishopMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves){
        //up-right
        for(int i = myPosition.getRow(), j = myPosition.getColumn(); j < 8 && i < 8;j++, i++){
            if (checkNull(board, new ChessPosition(i + 1, j +1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j +1), null));
            } else if (checkPieceColor(board, new ChessPosition(i + 1, j +1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j +1), null));
                break;
            } else {
                break;
            }
        }
        //down-left
        for(int i = myPosition.getRow(), j = myPosition.getColumn(); i > 1 && j > 1;j--, i--){
            if (checkNull(board, new ChessPosition(i - 1, j - 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j -1), null));
            } else if (checkPieceColor(board, new ChessPosition(i - 1, j -1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j -1), null));
                break;
            } else {
                break;
            }
        }

        //down-right
        for(int i = myPosition.getRow(), j = myPosition.getColumn(); i > 1 && j < 8 ; j++, i--){
            if (checkNull(board, new ChessPosition(i - 1, j +1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j +1), null));
            } else if (checkPieceColor(board, new ChessPosition(i - 1, j +1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i - 1, j +1), null));
                break;
            } else {
                break;
            }
        }
        //up-left
        for(int i = myPosition.getRow(), j = myPosition.getColumn(); j > 1 && i < 8; j--, i++){
            if (checkNull(board, new ChessPosition(i + 1, j - 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j -1), null));
            } else if (checkPieceColor(board, new ChessPosition(i + 1, j -1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(i + 1, j -1), null));
                break;
            } else {
                break;
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> knightMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves){
        int y = myPosition.getRow();
        int x = myPosition.getColumn();


        if(y + 2  <= 8 && x + 1  <= 8) {
            if (checkNull(board, new ChessPosition(y + 2, x + 1)) || checkPieceColor(board, new ChessPosition(y + 2, x + 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 2, x + 1), null));
            }
        }

        if(y + 2  <= 8 && x - 1 >= 1) {
            if (checkNull(board, new ChessPosition(y + 2, x - 1)) || checkPieceColor(board, new ChessPosition(y + 2, x - 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 2, x - 1), null));
            }
        }

        if(y + 1  <= 8 && x - 2 >= 1) {
            if (checkNull(board, new ChessPosition(y + 1, x - 2)) || checkPieceColor(board, new ChessPosition(y + 1, x - 2))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x - 2), null));
            }
        }

        if(y - 1 >= 1 && x - 2 >= 1) {
            if (checkNull(board, new ChessPosition(y - 1, x - 2)) || checkPieceColor(board, new ChessPosition(y - 1, x - 2))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x - 2), null));
            }
        }

        if(y - 2 >= 1 && x - 1 >= 1) {
            if (checkNull(board, new ChessPosition(y - 2, x - 1)) || checkPieceColor(board, new ChessPosition(y - 2, x - 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 2, x - 1), null));
            }
        }

        if(y - 2 >= 1 && x + 1  <= 8) {
            if (checkNull(board, new ChessPosition(y - 2, x + 1)) || checkPieceColor(board, new ChessPosition(y - 2, x + 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 2, x + 1), null));
            }
        }

        if(y - 1 >= 1 && x + 2  <= 8) {
            if (checkNull(board, new ChessPosition(y - 1, x + 2)) || checkPieceColor(board, new ChessPosition(y - 1, x + 2))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 2), null));
            }
        }

        if(y + 1  <= 8 && x + 2  <= 8) {
            if (checkNull(board, new ChessPosition(y + 1, x + 2)) || checkPieceColor(board, new ChessPosition(y + 1, x + 2))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x + 2), null));
            }
        }

        return validMoves;
    }

    private Collection<ChessMove> kingMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves){
        int y = myPosition.getRow();
        int x = myPosition.getColumn();

        //up-right
        if(y+1<= 8 && x+1 <= 8){
            if (checkNull(board, new ChessPosition(y + 1, x + 1)) || checkPieceColor(board, new ChessPosition(y + 1, x + 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y+ 1, x +1), null));
            }
        }

        //down-left
        if(y-1>=1 && x-1>=1) {
            if (checkNull(board, new ChessPosition(y - 1, x - 1)) || checkPieceColor(board, new ChessPosition(y - 1, x - 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x - 1), null));
            }
        }

        //down-right
        if(y-1>=1 && x+1<=8) {
            if (checkNull(board, new ChessPosition(y - 1, x + 1)) || checkPieceColor(board, new ChessPosition(y - 1, x + 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), null));
            }
        }

        //up-left
        if(y+1 <=8 && x-1>=1) {
            if (checkNull(board, new ChessPosition(y + 1, x - 1)) || checkPieceColor(board, new ChessPosition(y + 1, x - 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x - 1), null));
            }
        }

        //up
        if(y+1<=8) {
            if (checkNull(board, new ChessPosition(y + 1, myPosition.getColumn())) || checkPieceColor(board, new ChessPosition(y + 1, x))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, myPosition.getColumn()), null));
            }
        }

        //down
        if(y-1>=1) {
            if (checkNull(board, new ChessPosition(y - 1, myPosition.getColumn())) || checkPieceColor(board, new ChessPosition(y - 1, x))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, myPosition.getColumn()), null));
            }
        }

        //right
        if(x+1<=8) {
            if (checkNull(board, new ChessPosition(myPosition.getRow(), x + 1)) || checkPieceColor(board, new ChessPosition(y, x + 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), x + 1), null));
            }
        }

        //left
        if(x-1>=1) {
            if (checkNull(board, new ChessPosition(myPosition.getRow(), x - 1)) || checkPieceColor(board, new ChessPosition(y, x - 1))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), x - 1), null));
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> pawnMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves){
        int y = myPosition.getRow();
        int x = myPosition.getColumn();
        ////white
        if(pieceColor == ChessGame.TeamColor.WHITE) {
            //normal forward
            if (y + 1 < 8) {
                if (checkNull(board, new ChessPosition(y + 1, x))) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), null));
                }
            } else if(y+1 == 8){
                if (checkNull(board, new ChessPosition(y + 1, x))) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), PieceType.QUEEN ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), PieceType.ROOK ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), PieceType.BISHOP ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x), PieceType.KNIGHT ));
                }
            }
            //two move
            if (y == 2 && checkNull(board, new ChessPosition(y + 2, x)) && checkNull(board, new ChessPosition(y + 1, x))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 2, x), null));
            }
            //capture right
            if (y + 1 < 8 && x + 1 < 8) {
                if (!checkNull(board, new ChessPosition(y + 1, x + 1)) && (checkPieceColor(board, new ChessPosition(y + 1, x + 1)))) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x + 1), null));

                }
            }else if(y+1 == 8 && x + 1 < 8){
                if (!checkNull(board, new ChessPosition(y + 1, x+1))) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x+1), PieceType.QUEEN ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x+1), PieceType.ROOK ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x+1), PieceType.BISHOP ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x+1), PieceType.KNIGHT ));
                }
            }
            //capture left
            if (y + 1 < 8 && x - 1 > 1) {
                if (!checkNull(board, new ChessPosition(y + 1, x - 1)) && checkPieceColor(board, new ChessPosition(y + 1, x - 1))) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x - 1), null));
                }
            }else if(y+1 == 8 && x - 1 > 1){
                if (!checkNull(board, new ChessPosition(y + 1, x-1))) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x-1), PieceType.QUEEN ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x-1), PieceType.ROOK ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x-1), PieceType.BISHOP ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y + 1, x-1), PieceType.KNIGHT ));
                }
            }
        }
        ////black
        if(pieceColor == ChessGame.TeamColor.BLACK) {
            //normal forward
            if (y - 1 > 1) {
                if (checkNull(board, new ChessPosition(y - 1, x))) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), null));
                }
            } else if(y-1 == 1){
                if (checkNull(board, new ChessPosition(y - 1, x))) {
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), PieceType.QUEEN ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), PieceType.ROOK ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), PieceType.BISHOP ));
                    validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x), PieceType.KNIGHT ));
                }
            }
            //two move
            if (y == 7 && checkNull(board, new ChessPosition(y - 2, x)) && checkNull(board, new ChessPosition(y - 1, x))) {
                validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 2, x), null));
            }
            //capture right
            if (y - 1 > 1 && x + 1 < 8) {
                if (!checkNull(board, new ChessPosition(y - 1, x + 1)) && checkPieceColor(board, new ChessPosition(y - 1, x + 1))) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), null));
                }
            }else if(y-1 == 1 && x + 1 <= 8){
                if (!checkNull(board, new ChessPosition(y - 1, x + 1)) && checkPieceColor(board, new ChessPosition(y - 1, x + 1))) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), PieceType.QUEEN ));
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), PieceType.ROOK ));
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), PieceType.BISHOP ));
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x + 1), PieceType.KNIGHT ));
                }
            }
            //capture left
            if (y - 1 > 1 && x - 1 > 1) {
                if (!checkNull(board, new ChessPosition(y - 1, x - 1)) && checkPieceColor(board, new ChessPosition(y - 1, x - 1))) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x - 1), null));
                }
            }else if(y-1 == 1 && x - 1 >= 1){
                if (!checkNull(board, new ChessPosition(y - 1, x - 1)) && checkPieceColor(board, new ChessPosition(y - 1, x - 1))) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x-1), PieceType.QUEEN ));
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x-1), PieceType.ROOK ));
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x-1), PieceType.BISHOP ));
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(y - 1, x-1), PieceType.KNIGHT ));
                }
            }
        }
        return validMoves;
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

        switch(type) {
            case KING:
                validMoves.addAll(kingMove(board, myPosition, validMoves));

                break;


            case QUEEN:
                validMoves.addAll(bishopMove(board, myPosition, validMoves));
                validMoves.addAll(rookMove(board, myPosition, validMoves));
                break;


            case BISHOP:
                validMoves.addAll(bishopMove(board, myPosition, validMoves));
                break;


            case KNIGHT:
                validMoves.addAll(knightMove(board, myPosition, validMoves));
                break;


            case ROOK:
                validMoves.addAll(rookMove(board, myPosition, validMoves));
                break;


            case PAWN:
                validMoves.addAll(pawnMove(board, myPosition, validMoves));

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