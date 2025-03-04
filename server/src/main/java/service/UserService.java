package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import requestsAndResults.RegisterRequest;
import requestsAndResults.RegisterResult;

import javax.xml.crypto.Data;
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

    //	public LoginResult login(LoginRequest loginRequest) {}
    //	public void logout(LogoutRequest logoutRequest) {}

    public void clear(){
        userDB = new MemoryUserDAO();
        authDB = new MemoryAuthDAO();
    }
}
