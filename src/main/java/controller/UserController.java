package controller;

import annotation.Path;
import annotation.PrefixPath;
import com.google.gson.Gson;
import model.User;
import service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.util.List;

@PrefixPath(path = "/user/")
@Singleton
public class UserController implements Controller {
    private final UserService userService;

    class Temp{
        String userUUID;
    }

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Path(path = "/user/getallusers",type = "GET")
    public List<User> getAllUsers(BufferedReader requestBody) {
        return userService.getAllUsers();
    }

    @Path(path = "/user/getuser",type = "GET")
    public User getUser(BufferedReader requestBody) {
        return userService.getUser(getUUID(requestBody).userUUID);
    }

    @Path(path = "/user/adduser",type = "POST")
    public String addUser(BufferedReader requestBody) {
        return userService.addUser(getUserFromRequestBody(requestBody));
    }

    public String deleteUser(BufferedReader requestBody) {
        return userService.deleteUser(getUUID(requestBody).userUUID);
    }

    @Path(path = "/user/updateuser",type = "POST")
    public String updateUser(BufferedReader requestBody) {
        return userService.updateUser(getUserFromRequestBody(requestBody));
    }

    private Temp getUUID(BufferedReader requestBody){
        return new Gson().fromJson(requestBody, Temp.class);
    }

    private User getUserFromRequestBody(BufferedReader requestBody) {
        return new Gson().fromJson(requestBody, User.class);
    }
}
