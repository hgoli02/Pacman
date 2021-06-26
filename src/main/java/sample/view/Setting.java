package sample.view;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sample.controller.DatabaseController;
import sample.controller.GameController;
import sample.controller.LoginController;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Setting implements Initializable {

    public RadioButton easy;
    public RadioButton hard;
    public ChoiceBox mapBox;
    public Slider slider;
    public AnchorPane pane;
    private ToggleGroup toggleGroup;


    private static Setting instance;

    public static Setting getInstance() {
        if (instance == null) instance = new Setting();
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initialize called");
        toggleGroup = new ToggleGroup();
        easy.setToggleGroup(toggleGroup);
        hard.setToggleGroup(toggleGroup);
        HashMap<String, String> maps = DatabaseController.getMapsOfUser(LoginController.getInstance().getOnlineUser().getUsername());
        maps.forEach((key, value) -> mapBox.getItems().add(key));
    }

    public boolean isHard() {
        RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
        return selected == hard;
    }

    public String returnMap() {
        String name = (String) mapBox.getSelectionModel().getSelectedItem();
        return DatabaseController.getMaps().get(name);
    }

    public int getLife() {
        return (int) slider.getValue();
    }

    public void back(MouseEvent mouseEvent) {
        GameController.getInstance().setHard(isHard());
        GameController.getInstance().setMap(returnMap());
        GameController.getInstance().setLife(getLife());
        ViewSwitcher.switchTo(View.MAIN_MENU);
    }

    public void generateMap(MouseEvent mouseEvent) {
        String mapString = Utilities.createMaze(10,10);
        VBox map = Utilities.createGrid(mapString);
        map.setTranslateX(500);
        map.setTranslateY(10);
        pane.getChildren().add(map);
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setContentText("do you want to save this map? type 1 for yes");
        inputDialog.showAndWait();
        if (inputDialog.getEditor().getText().equals("1")){
            inputDialog.getEditor().clear();
            inputDialog.setContentText("enter a name");
            inputDialog.showAndWait();
            String name = inputDialog.getEditor().getText();
            LoginController.getInstance().getOnlineUser().addMap(name);
            DatabaseController.addMap(name, mapString);
            mapBox.getItems().add(name);
            DatabaseController.updateUser(LoginController.getInstance().getOnlineUser());
        }
    }
}
