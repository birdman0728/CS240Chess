package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.Objects;

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
            throw new DataAccessException("Auth does not exist");
        }
    }

    @Override
    public boolean verifyAuth(String authToken) throws DataAccessException {//TODO turn verify's into gets(?)
        if(Objects.equals(getDataFromAuth(authToken).authToken(), authToken)){
            return true;
        }
        throw new DataAccessException("Auth Does not exist");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM authdata WHERE authToken = ?";
        DatabaseManager.executeUpdate(statement, authToken);
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        int num;
        var statement = "SELECT COUNT(*) FROM authdata";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            var rs = ps.executeQuery();

            if(rs.next()){
                num = rs.getInt(1);
                return num == 0;
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new DataAccessException("Error while checking");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE authdata";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Did not clear");
        }
    }

}
