package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import requestsandresults.JoinRequest;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class SQLGameDAO implements GameDAO{
    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame newGame = new ChessGame();
        String serializedGame = serializeGame(newGame);
        var statement = "INSERT INTO gamedata (whiteUsername, blackUsername, gameName, game) values (?, ?, ?, ?)";
        return DatabaseManager.executeUpdate(statement, null, null, gameName, serializedGame);
    }

    @Override
    public void joinGame(JoinRequest request) throws DataAccessException {
        var statement = "";
        if(request.playerColor() == ChessGame.TeamColor.WHITE){
            statement = "UPDATE gamedata SET whiteUsername = (?) WHERE gameID = (?)";
        }else if (request.playerColor() == ChessGame.TeamColor.BLACK){
            statement = "UPDATE gamedata SET blackUsername = (?) WHERE gameID = (?)";
        }
        if(DatabaseManager.executeUpdate(statement, request.username(), request.gameID()) == 0){
            throw new DataAccessException("Didn't update");
        }
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

    private String serializeGame (ChessGame game){
        return new Gson().toJson(game);
    }


    @Override
    public void updateGame(GameData newGame, int gameID) throws DataAccessException {
        var statement = "UPDATE gamedata SET game = (?) WHERE gameID = (?)";
        if(DatabaseManager.executeUpdate(statement, serializeGame(newGame.game()), gameID) == 0){
            throw new DataAccessException("Didn't update");
        }
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        var statement = "DELETE gamedata WHERE gameID = (?)";
        DatabaseManager.executeUpdate(statement, gameID);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE gamedata";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Did not clear");
        }
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        int num;
        var statement = "SELECT COUNT(*) FROM gamedata";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            var rs = ps.executeQuery();

            if(rs.next()){
                num = rs.getInt(1);
                return num == 0;
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new DataAccessException("error while checking");
        }
    }
}
