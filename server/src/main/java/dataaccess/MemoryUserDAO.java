package dataaccess;

import model.UserData;
import requestsandresults.LoginRequest;

import java.util.HashSet;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    ////create
    HashSet<UserData> db = new HashSet<UserData>();
    public void createUser(UserData user) throws DataAccessException{
        for(UserData profile : db){
            if(user.equals(profile)){
                throw new DataAccessException("User Already exists");
            }
        }
        db.add(user);
    }

    public boolean verifyUser(LoginRequest request) throws DataAccessException{
        boolean found = false;
        for(UserData data: db){
            if(data.username().equals(request.username()) && data.password().equals(request.password())){
                found = true;
                break;
            }
        }
        if(!found) {
            throw new DataAccessException("unauthorized");
        }
        return found;
    }

    public boolean isEmpty (){
        return db.isEmpty();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(db, that.db);
    }

    @Override
    public int hashCode() {
        return Objects.hash(db);
    }
}
