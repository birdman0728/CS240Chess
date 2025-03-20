package dataaccess;

import model.AuthData;

import java.util.HashSet;

public class MemAuthDAO implements AuthDAO{
    HashSet<AuthData> db = new HashSet<>();

    public void createAuth(AuthData authData){
        db.add(authData);
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
    public void deleteAuth(String authToken) {
        db.removeIf(data -> data.authToken().equals(authToken));
    }

    @Override
    public void clear() {

    }

    public boolean isEmpty (){
        return db.isEmpty();
    }

}
