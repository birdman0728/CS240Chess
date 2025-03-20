package dataaccess;

import model.UserData;
import requestsandresults.LoginRequest;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, user.username(), user.password(), user.email());
    }

    @Override
    public boolean verifyUser(LoginRequest request) throws DataAccessException {
        var statement = "SELECT username FROM userdata WHERE username = ? AND password = ?";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            ps.setString(1, request.username());
            ps.setString(2, request.password());
            var rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new DataAccessException("User does not exist");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE userdata;";
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


    private final String[] createStatements = { //TODO set auth and game foreign keys correctly
            """
            CREATE TABLE IF NOT EXISTS  UserData (
                `username` varchar(256) NOT NULL,
                `password` varchar(256) NOT NULL,
                `email` varchar(256) NOT NULL,
                PRIMARY KEY (`username`)
                )
            """,
            """
            CREATE TABLE IF NOT EXISTS AuthData (
                `authToken` varchar(256) NOT NULL,
                `username` varchar(256) NOT NULL,
                PRIMARY KEY (`authToken`),
                INDEX(username)
                )
            """,
            """
            CREATE TABLE IF NOT EXISTS GameData (
                `gameID` int NOT NULL AUTO_INCREMENT,
                `whiteUsername` varchar(256),
                `blackUsername` varchar(256),
                `gameName` varchar(256) NOT NULL,
                `game` text NOT NULL,
                PRIMARY KEY (`gameID`)
                )
            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database: %s");//TODO make sure this throws a 500 level error
        }
    }
}
