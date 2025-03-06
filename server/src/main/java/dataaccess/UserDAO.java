package dataaccess;

import model.UserData;
import requestsandresults.LoginRequest;

public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;

    public boolean verifyUser(LoginRequest request) throws DataAccessException;

    public void updateUser() throws DataAccessException;

    public void deleteUser() throws DataAccessException;
}
