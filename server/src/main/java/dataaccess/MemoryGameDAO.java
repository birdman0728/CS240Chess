package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;
import java.util.Set;

public class MemoryGameDAO implements GameDAO{
    HashSet<GameData> db = new HashSet<GameData>();
    public int createGame(String gameName) {
        int id = newGameID();
        db.add(new GameData(id, null, null, gameName, new ChessGame()));
        return id;
    }

    int tracker = 0;
    private int newGameID(){
        tracker++;
        return tracker;
    }

    public GameData getGame(int gameID) throws DataAccessException{
        return null;
    }

    @Override
    public Set<GameData> getAllGames() {
        return new HashSet<>(db);
    }

    public void updateGame() throws DataAccessException{

    }

    public void deleteGame(int gameID) throws DataAccessException{

    }
}
