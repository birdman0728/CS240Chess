package dataaccess;

import chess.ChessGame;
import model.GameData;
import requestsandresults.JoinRequest;

import java.util.HashSet;
import java.util.Set;

public class MemGameDAO implements GameDAO{
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

    public void joinGame(JoinRequest request) throws DataAccessException{
        GameData oldData = getGame(request.gameID());
        String newUsername = request.username();
        if(request.playerColor() == ChessGame.TeamColor.BLACK && oldData.blackUsername() == null){
            updateGame(new GameData(request.gameID(), oldData.whiteUsername(), newUsername, oldData.gameName(), oldData.game()), request.gameID());
        }else if (request.playerColor() == ChessGame.TeamColor.WHITE && oldData.whiteUsername() == null){
            updateGame(new GameData(request.gameID(), newUsername, oldData.blackUsername(), oldData.gameName(), oldData.game()), request.gameID());
        }else{
            throw new DataAccessException("Error: already Taken");
        }
    }

    public GameData getGame(int gameID) throws DataAccessException{
        for(GameData data : db){
            if(data.gameID() == gameID){
                return data;
            }
        }
        throw new DataAccessException("Error: bad request");
    }

    public Set<GameData> getAllGames() {
        return new HashSet<>(db);
    }

    public void updateGame(GameData newGame, int gameID) throws DataAccessException{
        deleteGame(gameID);
        db.add(newGame);
    }

    public void deleteGame(int gameID) throws DataAccessException{
        db.remove(getGame(gameID));
    }

    public boolean isEmpty (){
        return db.isEmpty();
    }

}
