package sample.view;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.controller.GameController;
import sample.model.Cell;

import java.io.IOException;

public class GameView {

    public static final int X_CELL = 21;
    public static final int Y_CELL = 21;
    public Label score;
    public Label life;
    public AnimationTimer animationTimer;
    public ParallelTransition parallelTransition;
    Pane root;

    public void start(Stage primaryStage) throws IOException {
        System.out.println(getClass().getResource("downbox.fxml").toExternalForm());
        GridPane vBox = FXMLLoader.load(getClass().getResource("downbox.fxml"));
        BorderPane root = new BorderPane();
        this.root = root;
        primaryStage.setTitle("Pacman");
        primaryStage.setResizable(false);
        Scene scene = new Scene(root, Cell.CELL_SIZE * X_CELL, 50 + Cell.CELL_SIZE * Y_CELL);
        scene.setOnKeyPressed(this::movePacman);
        primaryStage.setScene(scene);
        primaryStage.show();
        createContent(root);
        root.setBottom(vBox);
        GameController.getInstance().init();
        createLabel(vBox);
        createLifeLabel(vBox);
    }

    private void createContent(Pane pane) {
        GameController.getInstance().initializeMusic();
        GameController.getInstance().setView(this);
        GameController.getInstance().createGrid(root);
        GameController.getInstance().initGhosts(root);
        KeyFrame ghostFrame = new KeyFrame(Duration.seconds(0.47), actionEvent -> {
            if (!GameController.getInstance().isPaused)
                GameController.getInstance().moveGhost();
        });
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.17), actionEvent2 -> {
            if (!GameController.getInstance().isPaused)
                GameController.getInstance().movePacmanFrame();
        });
        KeyFrame targetFrame = new KeyFrame(Duration.seconds(15), actionEvent -> {
            if (!GameController.getInstance().isPaused)
                GameController.getInstance().updateGhostTarget();
        });
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                GameController.getInstance().handleFrames();
            }
        };
        startAnimations(ghostFrame, keyFrame, targetFrame, animationTimer);
    }

    private void startAnimations(KeyFrame ghostFrame, KeyFrame keyFrame, KeyFrame targetFrame, AnimationTimer animationTimer) {
        animationTimer.start();
        parallelTransition = new ParallelTransition();
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(ghostFrame);
        Timeline timeline1 = new Timeline();
        timeline1.getKeyFrames().add(keyFrame);
        Timeline timeline2 = new Timeline();
        timeline2.getKeyFrames().add(targetFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline1.setCycleCount(Timeline.INDEFINITE);
        timeline2.setCycleCount(Timeline.INDEFINITE);
        parallelTransition.getChildren().addAll(timeline, timeline1);
        parallelTransition.play();
    }

    private void createLabel(GridPane pane) {
        score = new Label("0");
        score.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        score.setPrefHeight(45);
        score.setPrefWidth(143.0);
        score.setTextFill(Color.YELLOW);
        pane.add(score, 1, 1);
    }

    private void createLifeLabel(GridPane pane) {
        life = new Label(String.valueOf(GameController.getInstance().getLife()));
        life.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
        life.setPrefHeight(45);
        life.setPrefWidth(143.0);
        life.setTextFill(Color.YELLOW);
        pane.add(life, 3, 1);
    }

    private void movePacman(KeyEvent keyEvent) {
        GameController.getInstance().movePacman(keyEvent.getCode());
    }
}
