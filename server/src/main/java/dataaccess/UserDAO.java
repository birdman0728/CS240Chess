package dataaccess;

import model.UserData;

public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;

    public boolean verifyUser(String username, String Password) throws DataAccessException;

    public void updateUser() throws DataAccessException;

    public void deleteUser() throws DataAccessException;
}
