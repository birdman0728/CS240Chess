package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryGameDAO;
import model.GameData;
import requestsandresults.CreateRequest;
import requestsandresults.CreateResult;

import java.util.Set;

public class GameService {

    MemoryGameDAO gameDB = new MemoryGameDAO();

    public CreateResult createGame(CreateRequest request) throws DataAccessException {
        return new CreateResult(gameDB.createGame(request.gameName()));
    }

    public Set<GameData> listGames() {
        return gameDB.getAllGames();
    }

    public void clear() {
    gameDB = new MemoryGameDAO();
    }
}
