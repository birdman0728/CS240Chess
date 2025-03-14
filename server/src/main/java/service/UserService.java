package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import requestsandresults.*;

import java.util.UUID;

public class UserService {

//    UserDAO userDB = new MemUserDAO();
    UserDAO userDB = new SQLUserDAO();
//        AuthDAO authDB = new MemAuthDAO();
    AuthDAO authDB = new SQLAuthDAO();

    public UserService() throws DataAccessException {
    }

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

    public void clear(){//TODO revamp clear class
        userDB = new MemUserDAO();
        authDB = new MemAuthDAO();
    }
}
