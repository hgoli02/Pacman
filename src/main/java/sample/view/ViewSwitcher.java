package sample.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controller.GameController;
import sample.model.Cell;

import java.io.IOException;
import java.util.Objects;

public class ViewSwitcher {
    private static Stage stage;

    public static void switchTo(View view) {
        Parent root = null;
        try {
            System.out.println(ViewSwitcher.class.getResource(view.getFile()).toExternalForm());
            if (view != View.GAME && view != View.ScoreBoard)
                root = FXMLLoader.load(Objects.requireNonNull(ViewSwitcher.class.getResource(view.getFile())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (view) {
            case WELCOME -> {
                stage.setHeight(400);
                stage.setWidth(600);
            }
            case MAIN_MENU -> {
                stage.setHeight(600);
                stage.setWidth(800);
            }
            case SETTING -> {
                stage.setWidth(600);
                stage.setWidth(800);
            }
            case GAME -> {
                stage.setHeight(Cell.CELL_SIZE * GameController.Y_CELL + 90);
                stage.setWidth(Cell.CELL_SIZE * GameController.X_CELL);
                return;
            }
            case ScoreBoard -> {
                ScoreBoard.initScoreBoard(stage);
                return;
            }

        }
        assert root != null;
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ViewSwitcher.stage = stage;
    }
}
