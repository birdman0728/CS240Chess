package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{
    HashSet<AuthData> DB = new HashSet<AuthData>();

    public void createAuth(AuthData authData){
        DB.add(authData);
    }

    @Override
    public AuthData findAuth(String username) throws DataAccessException {
        for(AuthData data: DB){
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
