package service;

import dataaccess.userDAO;
import model.UserData;
import requestsAndResults.RegisterRequest;
import requestsAndResults.RegisterResult;

public class UserService {
    userDAO memoryDB = new userDAO();
    public RegisterResult register(RegisterRequest registerRequest){
        RegisterResult result = null;
        //TODO add error checking
        memoryDB.createUser(new UserData(registerRequest.username(),registerRequest.password(),registerRequest.email()));


        return result;
    }

    //	public LoginResult login(LoginRequest loginRequest) {}
    //	public void logout(LogoutRequest logoutRequest) {}
}
