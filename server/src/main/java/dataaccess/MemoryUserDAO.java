package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO {
    ////create
    HashSet<UserData> DB = new HashSet<UserData>();
    public void createUser(UserData user) throws DataAccessException{
        //TODO check it does exist
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
        throw new DataAccessException("Username doesn't exist");
    }

    public String findEmail(String email) throws DataAccessException{
        for(UserData data: DB){
            if(data.email().equals(email)){
                return data.email();
            }
        }
        throw new DataAccessException("Email doesn't exist");
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
