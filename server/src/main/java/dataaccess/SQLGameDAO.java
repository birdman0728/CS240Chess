package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import requestsandresults.JoinRequest;

import java.util.Set;

public class SQLGameDAO implements GameDAO{
    @Override
    public int createGame(String gameName) throws DataAccessException { //TODO when saving in SQL, JSON IT TO A STRING
        ChessGame newGame = new ChessGame();
        String serializedGame = new Gson().toJson(newGame);
        var statement = "INSERT INTO gamedata (whiteUsername, blackUsername, gameName, game) values (?, ?, ?, ?)";
        return DatabaseManager.executeUpdate(statement, null, null, gameName, serializedGame);
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
    public void clear(){

    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
