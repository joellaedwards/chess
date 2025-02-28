package service;
import model.*;


import handler.UserHandler;

public class UserService {
    public model.UserData register(UserHandler.RegisterRequest registerRequest) {



        return null;
    };

    public record RegisterResult(String username, String authToken) {};




}
