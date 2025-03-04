package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;
import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    ////create
    HashSet<UserData> DB = new HashSet<UserData>();
    public void createUser(UserData user) throws DataAccessException{
        //TODO check it does exist
        for(UserData profile : DB){
            if(user.equals(profile)){
                throw new DataAccessException("User Already exists");
            }
        }
        DB.add(user);
    }

//    @Override
//    public String findUser(UserData user) throws DataAccessException {
//        if(DB.contains(user)){
//            return
//        }
//    }

    //read
//    public Boolean verifyUsername(String username) throws DataAccessException{
//        boolean found = false;
//        for(UserData data: DB){
//            if(data.username().equals(username)){
//                found = true;
//                break;
//            }
//        }
//        if(!found) {
//            throw new DataAccessException("Username doesn't exist");
//        }
//        return found;
//    }
//
//    public Boolean verifyEmail(String email) throws DataAccessException{
//        boolean found = false;
//        for(UserData data: DB){
//            if(data.email().equals(email)){
//                found = true;
//            }
//        }
//        if(!found) {
//            throw new DataAccessException("Email doesn't exist");
//        }
//        return found;
//    }

    public boolean verifyUser(String username, String password) throws DataAccessException{
        boolean found = false;
        for(UserData data: DB){
            if(data.username().equals(username) && data.password().equals(password)){
                found = true;
                break;
            }
        }
        if(!found) {
            throw new DataAccessException("unauthorized");
        }
        return found;
    }

    ////update
    public void updateUser() throws DataAccessException{
        //error check
        //find user, create new user, delete old user
    }

    ////delete
    public void deleteUser() throws DataAccessException{
        //check if exists
        //find then remove
//        DB.remove()
    }


    //ex:void insertUser(UserData u) throws DataAccessException


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(DB, that.DB);
    }

    @Override
    public int hashCode() {
        return Objects.hash(DB);
    }
}
