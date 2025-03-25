package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemGameDAO;
import dataaccess.SQLGameDAO;
import model.GameData;
import requestsandresults.*;

import java.util.Set;

public class GameService {

    GameDAO gameDB = new SQLGameDAO();
//    GameDAO gameDB = new MemGameDAO();

    public CreateResult createGame(CreateRequest request) throws DataAccessException {
        return new CreateResult(gameDB.createGame(request.gameName()));
    }

    public JoinResult joinGame(JoinRequest request) throws DataAccessException{
        gameDB.joinGame(request);
        return new JoinResult();
    }

    public ListResult listGames() throws DataAccessException {
        return new ListResult(gameDB.getAllGames());
    }

    public GameData getGame(int gameID) throws DataAccessException{
        return gameDB.getGame(gameID);
    }

    public void clear() throws DataAccessException {
        gameDB.clear();
    }
}
