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
        String newToken = generateToken();
        userDB.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        authDB.createAuth(new AuthData(newToken, registerRequest.username()));

        return new RegisterResult(registerRequest.username(), newToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if(verifyUser(loginRequest)){//verifying user's username and password
            String newToken = generateToken();
            authDB.createAuth(new AuthData(newToken, loginRequest.username()));
            return new LoginResult(loginRequest.username(), newToken);
        }
        throw new DataAccessException("unauthorized");
    }
    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        if (verifyAuth(logoutRequest.authToken())) {
            authDB.deleteAuth(logoutRequest.authToken());
            return new LogoutResult();
        }
        throw new DataAccessException("unauthorized");
    }

    public boolean verifyUser(LoginRequest request) throws DataAccessException {
        return userDB.verifyUser(request);
    }

    public boolean verifyAuth(String authToken) throws DataAccessException {
        return authDB.verifyAuth(authToken);
    }

    public AuthData getUser(String authToken) throws DataAccessException {
        return authDB.getDataFromAuth(authToken);
    }

    public void clear(){
        userDB = new MemoryUserDAO();
        authDB = new MemoryAuthDAO();
    }
}
