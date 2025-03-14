package dataaccess;

import model.GameData;
import requestsandresults.JoinRequest;

import java.sql.SQLException;
import java.util.Set;

public class SQLGameDAO implements GameDAO{
    @Override
    public int createGame(String gameName) { //TODO when saving in SQL, JSON IT TO A STRING
        return 0;
    }

    @Override
    public void joinGame(JoinRequest request) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }


    public Set<GameData> getAllGames() {
        return null;
    }

    @Override
    public void updateGame(GameData newGame, int gameID) throws DataAccessException {

    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
