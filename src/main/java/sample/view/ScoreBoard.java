package sample.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sample.controller.ScoreBoardController;

public class ScoreBoard {

    public static void initScoreBoard(Stage stage) {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox(ScoreBoardController.getInstance().getList());
        Button button = new Button("back");
        button.setOnMouseClicked(mouseEvent -> back());
        hBox.setAlignment(Pos.BASELINE_CENTER);
        borderPane.setCenter(hBox);
        borderPane.setBottom(button);
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
    }

    private static void back() {
        ViewSwitcher.switchTo(View.MAIN_MENU);
    }
}
