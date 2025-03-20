package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO authdata (authToken, username) VALUES (?, ?)";
        DatabaseManager.executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getDataFromAuth(String authToken) throws DataAccessException {
        var statement = "SELECT username FROM authdata WHERE authToken = ? ";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            ps.setString(1, authToken);
            var rs = ps.executeQuery();

            while(rs.next()){
                return new AuthData(authToken, rs.getString("username"));
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new DataAccessException("User does not exist");
        }
    }

    @Override
    public boolean verifyAuth(String authToken) throws DataAccessException {
        return false;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE authdata;";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Did not clear");
        }
    }

}
