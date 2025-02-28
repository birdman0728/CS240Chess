package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashSet;

public class userDAO {
    //TODO: create a "database" to add to or edit from etc.
    //create
    HashSet<UserData> DB = new HashSet<UserData>();
    public void createUser(UserData user){
        DB.add(user);
    }
    //read
    //update
    //delete


    //ex:void insertUser(UserData u) throws DataAccessException
}
