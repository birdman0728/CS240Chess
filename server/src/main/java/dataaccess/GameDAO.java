package dataaccess;

import model.GameData;

import java.util.Set;

public interface GameDAO {
    public int createGame(String gameName);

    public GameData getGame(int gameID) throws DataAccessException;

    public Set<GameData> getAllGames();

    public void updateGame(GameData newGame, int gameID) throws DataAccessException;

    public void deleteGame(int gameID) throws DataAccessException;
}
