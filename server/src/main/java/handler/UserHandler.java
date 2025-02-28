package handler;

import com.google.gson.Gson;
import service.UserService;

public class UserHandler {
    public record RegisterRequest(String username, String password, String email) {};

    public UserService.RegisterResult registerHandler(String requestBody) {
        var serializer = new Gson();
        var registerReq = new RegisterRequest();
        var json = serializer.toJson(requestBody);

        RegisterRequest registerReq = new RegisterRequest();

        String username = null;

        var json = serializer.fromJson

        return new UserService.register()
    }



}
