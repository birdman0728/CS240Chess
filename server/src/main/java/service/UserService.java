package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import requestsandresults.*;

import java.util.UUID;

public class UserService {
    MemoryUserDAO userDB = new MemoryUserDAO();
    MemoryAuthDAO authDB = new MemoryAuthDAO();

    private String generateToken(){
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException{
        //TODO add error checking
        String newToken = generateToken();
        userDB.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        authDB.createAuth(new AuthData(newToken, registerRequest.username()));

        return new RegisterResult(registerRequest.username(), newToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if(userDB.verifyUser(loginRequest.username(), loginRequest.password())){//verifying user's username and password
            return new LoginResult(loginRequest.username(), authDB.findAuth(loginRequest.username()).authToken());
        }
        throw new DataAccessException("unauthorized");
    }
    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        if (authDB.verifyAuth(logoutRequest.authToken())) {
            authDB.deleteAuth(logoutRequest.authToken());
            return new LogoutResult();
        }
        throw new DataAccessException("unauthorized");
    }

    public void clear(){
        userDB = new MemoryUserDAO();
        authDB = new MemoryAuthDAO();
    }
}
