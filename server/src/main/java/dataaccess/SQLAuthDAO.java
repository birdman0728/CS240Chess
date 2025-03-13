package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{

    @Override
    public void createAuth(AuthData authData) {

    }

    @Override
    public AuthData getDataFromAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public boolean verifyAuth(String authToken) throws DataAccessException {
        return false;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

}
