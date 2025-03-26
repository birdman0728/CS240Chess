package dataaccess;

import model.UserData;
import requestsandresults.LoginRequest;

public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;

    public boolean verifyUser(LoginRequest request) throws DataAccessException;

    void clear() throws DataAccessException;

    boolean isEmpty() throws DataAccessException;
}
