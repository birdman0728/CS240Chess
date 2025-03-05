package dataaccess;

import model.AuthData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{
    HashSet<AuthData> db = new HashSet<AuthData>();

    public void createAuth(AuthData authData){
        db.add(authData);
    }

    @Override
    public AuthData findAuth(String username) throws DataAccessException {
        for(AuthData data: db){
            if(data.username().equals(username)){
                return data;
            }
        }
        throw new DataAccessException("unauthorized");
    }

    @Override
    public void updateAuth() throws DataAccessException {

    }

    @Override
    public void deleteAuth() throws DataAccessException {

    }

}
