package dataaccess;

import model.UserData;
import requestsandresults.LoginRequest;

import java.util.HashSet;
import java.util.Objects;

public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;

    public boolean verifyUser(LoginRequest request) throws DataAccessException;
}
