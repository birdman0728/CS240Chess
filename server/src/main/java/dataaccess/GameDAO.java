package dataaccess;

import chess.ChessGame;
import model.GameData;
import requestsandresults.JoinRequest;

import java.util.HashSet;
import java.util.Set;

public interface GameDAO {
    public int createGame(String gameName);

    public void joinGame(JoinRequest request) throws DataAccessException;

    public GameData getGame(int gameID) throws DataAccessException;

    public void updateGame(GameData newGame, int gameID) throws DataAccessException;

    public void deleteGame(int gameID) throws DataAccessException;

    public boolean isEmpty ();

    Set<GameData> getAllGames();
}
