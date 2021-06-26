package sample.controller;

import sample.model.User;

public class LoginController {
    private static LoginController instance;
    private User onlineUser;

    public static LoginController getInstance() {
        if (instance == null) instance = new LoginController();
        return instance;
    }

    public User getOnlineUser() {
        return onlineUser;
    }

    public void signUp(String username, String password) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("empty field!");
        } else if (DatabaseController.getUserByName(username) != null) {
            throw new Exception("user already exists");
        }
        DatabaseController.updateUser(new User(username, password));
    }

    public void login(String username, String password) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("empty field!");
        }
        User user = DatabaseController.getUserByName(username);
        if (user == null) {
            throw new Exception("no user exists with this username");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("wrong password");
        }
        onlineUser = user;
    }

    public void changePassword(String username, String password, String oldPassword) throws Exception {
        if (username.isEmpty() || password.isEmpty() || oldPassword.isEmpty()) {
            throw new Exception("empty field!");
        }
        User user = DatabaseController.getUserByName(username);
        if (user == null) {
            throw new Exception("no user exists with this username");
        }
        if (!user.getPassword().equals(oldPassword)) {
            throw new Exception("old password is wrong");
        }
        user.setPassword(password);
        DatabaseController.updateUser(user);
    }

    public void removeAccount(String username, String password) throws Exception {
        if (username.isEmpty() || password.isEmpty()) {
            throw new Exception("empty field!");
        }
        User user = DatabaseController.getUserByName(username);
        if (user == null) {
            throw new Exception("no user exists with this username");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception("password is wrong");
        }
        DatabaseController.removeUser(user);
    }
}
