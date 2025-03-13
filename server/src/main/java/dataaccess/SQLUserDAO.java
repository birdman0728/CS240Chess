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

    }

    @Override
    public boolean verifyUser(LoginRequest request) throws DataAccessException {
        return false;
    }


    private final String[] createStatements = { //TODO create proper statement
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
