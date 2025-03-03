package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO {
    ////create
    HashSet<UserData> DB = new HashSet<UserData>();
    public void createUser(UserData user){
        //TODO check it doesn't exist
        DB.add(user);
    }

    ////read
    public String findUsername(String username) throws DataAccessException{
        //error check
        for(UserData data: DB){
            if(data.username().equals(username)){
                return data.username();
            }
        }
        //TODO error throwing?
        return null;
    }

    public String findEmail(String email) throws DataAccessException{
        //error check
        for(UserData data: DB){
            if(data.email().equals(email)){
                return data.email();
            }
        }
        return null;
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
