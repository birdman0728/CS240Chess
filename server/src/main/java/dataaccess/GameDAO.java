package dataaccess;

import chess.ChessGame;
import model.GameData;
import requestsandresults.JoinRequest;

import java.util.HashSet;
import java.util.Set;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException;

    void joinGame(JoinRequest request) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    void updateGame(GameData newGame, int gameID) throws DataAccessException;

    void deleteGame(int gameID) throws DataAccessException;

    void clear();

    boolean isEmpty ();

    Set<GameData> getAllGames();
}
