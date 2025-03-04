package dataaccess;

import model.UserData;

public interface UserDAO {
    public void createUser(UserData user) throws DataAccessException;

    public String findUsername(String username) throws DataAccessException;

    public String findEmail(String email) throws DataAccessException;

    public void updateUser() throws DataAccessException;

    public void deleteUser() throws DataAccessException;
}
