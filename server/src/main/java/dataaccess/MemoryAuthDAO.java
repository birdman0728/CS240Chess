package dataaccess;

import model.AuthData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{
    HashSet<AuthData> db = new HashSet<AuthData>();

    public void createAuth(AuthData authData){
        db.add(authData);
    }

    @Override
    public AuthData findAuthFromUser(String username) throws DataAccessException {
        for(AuthData data: db){
            if(data.username().equals(username)){
                return data;
            }
        }
        throw new DataAccessException("unauthorized");
    }

    public AuthData getDataFromAuth(String authToken) throws DataAccessException{
        for(AuthData data: db){
            if(data.authToken().equals(authToken)){
                return data;
            }
        }
        throw new DataAccessException("unauthorized");
    }

    public boolean verifyAuth(String authToken) throws DataAccessException {
        for(AuthData data: db){
            if (data.authToken().equals(authToken)){
                return true;
            }
        }
        throw new DataAccessException("unauthorized");
    }

    @Override
    public void updateAuth() throws DataAccessException {

    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        db.removeIf(data -> data.authToken().equals(authToken));
    }

}
