package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.MemGameDAO;
import model.GameData;
import requestsandresults.*;

import java.util.Set;

public class GameService {

    GameDAO gameDB = new MemGameDAO();

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

    public void clear() {
    gameDB = new MemGameDAO();
    }
}
