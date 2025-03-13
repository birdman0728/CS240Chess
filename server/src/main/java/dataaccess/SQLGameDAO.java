package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.Set;

public class SQLGameDAO implements GameDAO{
    @Override
    public int createGame(String gameName) { //TODO when saving in SQL, JSON IT TO A STRING
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Set<GameData> getAllGames() {
        return null;
    }

    @Override
    public void updateGame(GameData newGame, int gameID) throws DataAccessException {

    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {

    }
}
