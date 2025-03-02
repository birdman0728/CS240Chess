package service;

import dataaccess.MemoryUserDAO;
import model.UserData;
import requestsAndResults.RegisterRequest;
import requestsAndResults.RegisterResult;

public class UserService {
    MemoryUserDAO memoryDB = new MemoryUserDAO();

    public RegisterResult register(RegisterRequest registerRequest){
        RegisterResult result = null;
        //TODO add error checking
        memoryDB.createUser(new UserData(registerRequest.username(),registerRequest.password(),registerRequest.email()));


        return result;
    }

    //	public LoginResult login(LoginRequest loginRequest) {}
    //	public void logout(LogoutRequest logoutRequest) {}
}
