package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requestsandresults.LoginRequest;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO userdata (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(statement, user.username(), hashPassword(user.password()), user.email());
    }

    private String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean verifyUser(LoginRequest request) throws DataAccessException {
        var statement = "SELECT password FROM userdata WHERE username = ?";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            ps.setString(1, request.username());
            var rs = ps.executeQuery();

            if(rs.next()){
                String hashpass = rs.getString("password");
                return BCrypt.checkpw(request.password(), hashpass);
            }
            throw new SQLException();
        } catch (SQLException e) {
            throw new DataAccessException("User does not exist");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE userdata";
        try(var ps = DatabaseManager.getConnection().prepareStatement(statement)){
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Did not clear");
        }
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        int num;
        var statement = "SELECT COUNT(*) FROM userdata";
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


    private final String[] createStatements = { //TODO set auth and game foreign keys correctly
            """
            CREATE TABLE IF NOT EXISTS  userdata (
                `username` varchar(256) NOT NULL,
                `password` varchar(256) NOT NULL,
                `email` varchar(256) NOT NULL,
                PRIMARY KEY (`username`)
                )
            """,
            """
            CREATE TABLE IF NOT EXISTS authdata (
                `authToken` varchar(256) NOT NULL,
                `username` varchar(256) NOT NULL,
                PRIMARY KEY (`authToken`),
                INDEX(username)
                )
            """,
            """
            CREATE TABLE IF NOT EXISTS gamedata (
                `gameID` int NOT NULL AUTO_INCREMENT,
                `whiteUsername` varchar(256),
                `blackUsername` varchar(256),
                `gameName` varchar(256) NOT NULL,
                `game` text NOT NULL,
                PRIMARY KEY (`gameID`)
                )
            """
    };
//    CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES UserData(username)


    //                CONSTRAINT fk_whiteUsername FOREIGN KEY (whiteUsername) REFERENCES UserData(username),
    //                CONSTRAINT fk_blackUsername FOREIGN KEY (blackUsername) REFERENCES UserData(username)


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
