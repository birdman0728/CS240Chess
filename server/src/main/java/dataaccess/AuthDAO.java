package dataaccess;

import model.AuthData;

public interface AuthDAO {

    public void createAuth(AuthData authData) throws DataAccessException;
    public AuthData getDataFromAuth(String authToken) throws DataAccessException;

    public boolean verifyAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken);

    boolean isEmpty();
}
