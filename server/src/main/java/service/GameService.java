package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.GameData;
import requestsandresults.*;

import java.util.Set;

public class GameService {

    MemoryGameDAO gameDB = new MemoryGameDAO();

    public CreateResult createGame(CreateRequest request) throws DataAccessException {
        return new CreateResult(gameDB.createGame(request.gameName()));
    }

    public JoinResult joinGame(JoinRequest request) throws DataAccessException{
        gameDB.joinGame(request);
        return new JoinResult();
    }

    public ListResult listGames() {
        return new ListResult(gameDB.getAllGames());
    }

    public void clear() {
    gameDB = new MemoryGameDAO();
    }
}
