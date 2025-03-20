package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import requestsandresults.JoinRequest;

import java.sql.SQLException;
import java.util.HashSet;
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
        var statement = "SELECT * FROM gamedata WHERE gameID = ? ";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            ps.setInt(1, gameID);
            var rs = ps.executeQuery();

            while(rs.next()){
                gameID = rs.getInt("gameID");
                String wtusername = rs.getString("whiteUsername");
                String bkusername = rs.getString("blackUsername");
                String gamename = rs.getString("gameName");
                ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                return new GameData(gameID, wtusername, bkusername, gamename, game);
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new DataAccessException("Game does not exist");
        }
    }


    public Set<GameData> getAllGames() throws DataAccessException {
        Set<GameData> allGames = new HashSet<>();
        var statement = "SELECT * FROM gamedata";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            var rs = ps.executeQuery();

            while(rs.next()){
                int gameID = rs.getInt("gameID");
                String wtusername = rs.getString("whiteUsername");
                String bkusername = rs.getString("blackUsername");
                String gamename = rs.getString("gameName");
                ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                GameData newGame = new GameData(gameID, wtusername, bkusername, gamename, game);
                allGames.add(newGame);
            }
            return allGames;
        } catch (SQLException e) {
            throw new DataAccessException("Game does not exist");
        }
    }

    //TODO list 0 games

    @Override
    public void updateGame(GameData newGame, int gameID) throws DataAccessException {

    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE gamedata;";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Did not clear");
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
