package sample.controller;

import javafx.scene.control.ListView;
import sample.model.User;

import java.util.List;

public class ScoreBoardController {

    private static ScoreBoardController instance;

    public static ScoreBoardController getInstance() {
        if (instance == null) instance = new ScoreBoardController();
        return instance;
    }

    public ListView<String> getList() {
        ListView<String> listView = new ListView<>();
        List<User> userList = DatabaseController.getAllUsers();
        userList.sort(User::compareTo);
        for (User user : userList) {
            listView.getItems().add(user.getUsername() + " : " + user.getScore());
        }
        return listView;
    }
}
