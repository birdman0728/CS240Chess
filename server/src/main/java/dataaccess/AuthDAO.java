package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getDataFromAuth(String authToken) throws DataAccessException;

    boolean verifyAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
    boolean isEmpty();
}
