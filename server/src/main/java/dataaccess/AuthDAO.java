package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {

    public void createAuth(AuthData auth) throws DataAccessException;

    public AuthData findAuth(String username) throws DataAccessException;

    public void updateAuth() throws DataAccessException;

    public void deleteAuth() throws DataAccessException;
}
