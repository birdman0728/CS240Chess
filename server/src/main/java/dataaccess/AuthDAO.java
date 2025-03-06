package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public void createAuth(AuthData auth) throws DataAccessException;

    public AuthData findAuthFromUser(String username) throws DataAccessException;
    public boolean verifyAuth(String authToken) throws DataAccessException;

    public void updateAuth() throws DataAccessException;

    public void deleteAuth(String authToken) throws DataAccessException;
}
