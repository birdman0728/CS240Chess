package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard curBoard = new ChessBoard();
    TeamColor TeamTurn = TeamColor.WHITE;
    public ChessGame() {
        curBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return TeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        TeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private TeamColor oppColor(TeamColor color){
        if(color == TeamColor.WHITE){
            return TeamColor.BLACK;
        }else{
            return TeamColor.WHITE;
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPiece originalPiece = curBoard.getPiece(startPosition);
        ChessPiece endPiece;
        TeamColor checkColor = curBoard.getPiece(startPosition).getTeamColor();
        for(ChessMove move : curBoard.getPiece(startPosition).pieceMoves(curBoard,startPosition)){
            if(curBoard.getPiece(move.getEndPosition()) != null){
                endPiece = curBoard.getPiece(move.getEndPosition());
            }else{
                endPiece = null;
            }

            movePiece(move);
            if(!isInCheck(checkColor)){
                validMoves.add(move);
            }
            unmovePiece(move,endPiece);
        }
        return validMoves;
    }

    private void unmovePiece(ChessMove move, ChessPiece replacementPiece){
        ChessPiece oldPiece;
        if(move.getPromotionPiece() == null){
            oldPiece = new ChessPiece(curBoard.getPiece(move.getEndPosition()).getTeamColor(), curBoard.getPiece(move.getEndPosition()).getPieceType());
        }else{
            oldPiece = new ChessPiece(curBoard.getPiece(move.getEndPosition()).getTeamColor(), ChessPiece.PieceType.PAWN);
        }

        curBoard.addPiece(move.getStartPosition(), oldPiece);
        if(replacementPiece != null){
            curBoard.addPiece(move.getEndPosition(), replacementPiece);
        }else{
            curBoard.addPiece(move.getEndPosition(), null);
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //Check if can, throw if can't
        ChessPiece startPiece = curBoard.getPiece(move.getStartPosition());
        if(startPiece == null){
            throw new InvalidMoveException("No starting piece");
        }else if(!validMoves(move.startPosition).contains(move)){
            throw new InvalidMoveException("Illegal move");
        }else if(startPiece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("Not your turn");
        }

        movePiece(move);
        setTeamTurn(oppColor(getTeamTurn()));
    }

    private void movePiece(ChessMove move) {
        ChessPiece newPiece;
        if(move.getPromotionPiece() == null){
            newPiece = new ChessPiece(curBoard.getPiece(move.startPosition).getTeamColor(), curBoard.getPiece(move.getStartPosition()).getPieceType());
        }else{
            newPiece = new ChessPiece(curBoard.getPiece(move.startPosition).getTeamColor(), move.getPromotionPiece());
        }

        curBoard.addPiece(move.getEndPosition(), newPiece);
        curBoard.addPiece(move.getStartPosition(), null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingsSpot = findKing(teamColor);
        for(ChessMove move : findAllPossibleTeamMoves(oppColor(teamColor))){
            if(move.getEndPosition().equals(kingsSpot)){
                return true;
            }
        }
        return false;
    }

    private ChessPosition findKing(TeamColor color){
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPiece newPiece = curBoard.getPiece(new ChessPosition(i,j));
                if(newPiece != null
                        && newPiece.getPieceType() == ChessPiece.PieceType.KING
                        && newPiece. getTeamColor() == color){
                    return new ChessPosition(i,j);
                }
            }
        }
        return null;
    }

    private Collection <ChessMove> findAllPossibleTeamMoves(TeamColor teamColor){
        Collection<ChessMove> possibleMoves = new HashSet<>();
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition newPos = new ChessPosition(i,j);
                if(curBoard.getPiece(newPos) != null && curBoard.getPiece(newPos).pieceColor == teamColor) {
                    possibleMoves.addAll(curBoard.getPiece(newPos).pieceMoves(curBoard, newPos));
                }
            }
        }
        return possibleMoves;
    }

    private Collection <ChessMove> findAllVerifiedMoves(TeamColor teamColor){
        Collection<ChessMove> verified = new HashSet<>();
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition newPos = new ChessPosition(i,j);
                if(curBoard.getPiece(newPos) != null && curBoard.getPiece(newPos).pieceColor == teamColor) {
                    verified.addAll(validMoves(newPos));
                }
            }
        }
        return verified;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && findAllVerifiedMoves(teamColor).isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && findAllVerifiedMoves(teamColor).isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.curBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return curBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(curBoard, chessGame.curBoard) && TeamTurn == chessGame.TeamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(curBoard, TeamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "curBoard=" + curBoard +
                ", TeamTurn=" + TeamTurn +
                '}';
    }
}
