package dataaccess;

import model.UserData;

public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;

    public Boolean verifyUsername(String username) throws DataAccessException;

    public Boolean verifyEmail(String email) throws DataAccessException;

    public void updateUser() throws DataAccessException;

    public void deleteUser() throws DataAccessException;
}
