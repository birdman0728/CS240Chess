package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashSet;

public class MemoryAuthDAO {
    HashSet<AuthData> DB = new HashSet<AuthData>();

    public void createAuth(AuthData authData){
        DB.add(authData);
    }

}
