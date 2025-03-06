package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {

    public void createAuth(AuthData auth) throws DataAccessException;

    public AuthData findAuth(String username) throws DataAccessException;
    public boolean verifyAuth(String authToken) throws DataAccessException;

    public void updateAuth() throws DataAccessException;

    public void deleteAuth(String authToken) throws DataAccessException;
}
