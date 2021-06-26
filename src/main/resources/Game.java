//package sample.test;
//
//import javafx.animation.AnimationTimer;
//import javafx.animation.KeyFrame;
//import javafx.animation.ParallelTransition;
//import javafx.animation.Timeline;
//import javafx.application.Application;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import sample.controller.DatabaseController;
//import sample.controller.LoginController;
//import sample.model.Cell;
//import sample.model.Ghost;
//import sample.model.Pacman;
//import sample.model.User;
//import sample.view.View;
//import sample.view.ViewSwitcher;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Random;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//public class Game extends Application {
//    public static final int X_CELL = 21;
//    public static final int Y_CELL = 21;
//    public static Random random = new Random();
//    private static boolean isCrazy = false;
//    private static boolean isHard = true;
//    public Label score;
//    public boolean test;
//    ParallelTransition parallelTransition;
//    Pacman pacman;
//    Pane root;
//    private int crazyCounter = 0;
//    private int lifePoints = 2;
//    private Label life;
//    private int scorePoint;
//    private int numberOfPoints;
//    private ArrayList<Ghost> ghosts = new ArrayList<>();
//    private Cell[][] grid = new Cell[21][21];
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    public static boolean isCrazyMode() {
//        return isCrazy;
//    }
//
//    public static void setIsCrazy(boolean isCrazy) {
//        Game.isCrazy = isCrazy;
//    }
//
//    public static boolean isHard() {
//        return isHard;
//    }
//
//    public void setIsHard(boolean isHard) {
//        Game.isHard = isHard;
//    }
//
//    public void setLifePoints(int lifePoints) {
//        this.lifePoints = lifePoints;
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//        GridPane vBox = FXMLLoader.load(getClass().getResource("/sample/downbox.fxml"));
//        createLabel(vBox);
//        createLifeLabel(vBox);
//        BorderPane root = new BorderPane();
//        this.root = root;
//        primaryStage.setTitle("Pacman");
//        primaryStage.setResizable(false);
//        Scene scene = new Scene(root, Cell.CELL_SIZE * X_CELL, 50 + Cell.CELL_SIZE * Y_CELL);
//        scene.setOnKeyPressed(this::movePacman);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        createContent(root, createMap());
//        root.setBottom(vBox);
//    }
//
//    private void movePacman(KeyEvent keyEvent) {
//        switch (keyEvent.getCode()) {
//            case W -> pacman.setDirection(Pacman.Direction.UP);
//            case D -> pacman.setDirection(Pacman.Direction.RIGHT);
//            case A -> pacman.setDirection(Pacman.Direction.LEFT);
//            case S -> pacman.setDirection(Pacman.Direction.DOWN);
//            case K -> test = true;
//        }
//    }
//
//    private void createContent(Pane pane, boolean[][] map) {
//        for (int i = 0; i < Y_CELL; i++) {
//            for (int j = 0; j < X_CELL; j++) {
//                Cell cell = new Cell(j, i, map[i][j]);
//                grid[i][j] = cell;
//                if (cell.isPoint()) {
//                    numberOfPoints++;
//                }
//                pane.getChildren().add(cell);
//            }
//        }
//        pacman = new Pacman();
//        Ghost orange = new Ghost(Ghost.GhostColor.YELLOW, (X_CELL - 2), Y_CELL - 1, grid);
//        Ghost red = new Ghost(Ghost.GhostColor.RED, X_CELL - 1, 2, grid);
//        Ghost blue = new Ghost(Ghost.GhostColor.BLUE, 1, Y_CELL - 2, grid);
//        ghosts.add(orange);
//        ghosts.add(red);
//        ghosts.add(blue);
//        pane.getChildren().add(pacman);
//        for (Ghost aGhost : ghosts) {
//            pane.getChildren().add(aGhost);
//        }
//        KeyFrame ghostFrame = new KeyFrame(Duration.seconds(0.47), actionEvent -> {
//            if (test) {
//                System.out.println("hi");
//            }
//            for (Ghost ghost : ghosts) {
//                if (ghost.isActive()) {
//                    ghost.moveGhost(pacman);
//                }
//            }
//        });
//        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.17), actionEvent2 -> {
//            if (pacman.canMove(grid)) {
//                switch (pacman.getDirection()) {
//                    case UP -> pacman.changeY(-1);
//                    case DOWN -> pacman.changeY(+1);
//                    case LEFT -> pacman.changeX(-1);
//                    case RIGHT -> pacman.changeX(+1);
//                }
//                System.out.println(pacman.toString());
//            }
//        });
//        KeyFrame targetFrame = new KeyFrame(Duration.seconds(15), actionEvent -> {
//            for (Ghost ghost : ghosts) {
//                ghost.updateGhostTarget(pacman);
//            }
//        });
//        AnimationTimer animationTimer = new AnimationTimer() {
//            @Override
//            public void handle(long l) {
//                if (grid[pacman.getYCoordinate()][pacman.getXCoordinate()].isPoint()) {
//                    numberOfPoints--;
//                    changeScore();
//                    grid[pacman.getYCoordinate()][pacman.getXCoordinate()].setPoint(false);
//                } else if (grid[pacman.getYCoordinate()][pacman.getXCoordinate()].isEnergyBomb()) {
//                    changeScore();
//                    grid[pacman.getYCoordinate()][pacman.getXCoordinate()].removeEnergyBomb();
//                    for (Ghost ghost : ghosts) {
//                        ghost.setGhostMode();
//                    }
//                    new java.util.Timer().schedule(
//                            new java.util.TimerTask() {
//                                @Override
//                                public void run() {
//                                    for (Ghost ghost : ghosts) {
//                                        ghost.resetGhostMode();
//                                    }
//                                    crazyCounter = 0;
//                                }
//                            },
//                            10000
//                    );
//
//                }
//                if (numberOfPoints == 0) {
//                    System.out.println("WIN");
//                    System.exit(100);
//                }
//                for (Ghost ghost : ghosts) {
//                    if (pacman.getXCoordinate() == ghost.getXCoordinate() &&
//                            pacman.getYCoordinate() == ghost.getYCoordinate()) {
//                        if (isCrazy) {
//                            eatGhost(ghost);
//                        } else {
//                            finishGame();
//                        }
//                    }
//                }
//            }
//        };
//        startAnimations(ghostFrame, keyFrame, targetFrame, animationTimer);
//    }
//
//    private void eatGhost(Ghost ghost) {
//        crazyCounter++;
//        updateCrazyCounterScore();
//        ghost.setActive(false);
//        new Timer().schedule(
//                new TimerTask() {
//                    @Override
//                    public void run() {
//                        ghost.setActive(true);
//                    }
//                },
//                4000
//        );
//        ghost.resetPosition();
//    }
//
//    private void finishGame() {
//        lifePoints--;
//        if (lifePoints == 0) {
//            User onlineUser = LoginController.getInstance().getOnlineUser();
//            onlineUser.increaseScore(scorePoint);
//            DatabaseController.updateUser(onlineUser);
//            ViewSwitcher.switchTo(View.MAIN_MENU);
//        }
//        life.setText(String.valueOf(lifePoints));
//        pacman.resetPosition();
//    }
//
//    private void startAnimations(KeyFrame ghostFrame, KeyFrame keyFrame, KeyFrame targetFrame, AnimationTimer animationTimer) {
//        animationTimer.start();
//        parallelTransition = new ParallelTransition();
//        Timeline timeline = new Timeline();
//        timeline.getKeyFrames().add(ghostFrame);
//        Timeline timeline1 = new Timeline();
//        timeline1.getKeyFrames().add(keyFrame);
//        Timeline timeline2 = new Timeline();
//        timeline2.getKeyFrames().add(targetFrame);
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline1.setCycleCount(Timeline.INDEFINITE);
//        timeline2.setCycleCount(Timeline.INDEFINITE);
//        parallelTransition.getChildren().addAll(timeline, timeline1);
//        parallelTransition.play();
//    }
//
//    @FXML
//    private void changeScore() {
//        scorePoint++;
//        score.setText(String.format("%d", Integer.parseInt(score.getText()) + 1));
//    }
//
//    private void updateCrazyCounterScore() {
//        int points = crazyCounter * 200;
//        score.setText(String.format("%d", Integer.parseInt(score.getText()) + points));
//    }
//
//    private void createLabel(GridPane pane) {
//        score = new Label("0");
//        score.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
//        score.setPrefHeight(45);
//        score.setPrefWidth(143.0);
//        score.setTextFill(Color.YELLOW);
//        pane.add(score, 1, 1);
//    }
//
//    private void createLifeLabel(GridPane pane) {
//        life = new Label(String.valueOf(lifePoints));
//        life.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
//        life.setPrefHeight(45);
//        life.setPrefWidth(143.0);
//        life.setTextFill(Color.YELLOW);
//        pane.add(life, 3, 1);
//    }
//
//    public boolean[][] createMap() {
//        String map = """
//                1e1111111111111111111
//                1*1*0*0*0*1*0*1*0*0*1
//                101011111010111111101
//                1*1*0*0*1*0*1*1*0*0*1
//                100000000000001000001
//                100111111111111110001
//                100000011100000000001
//                101111111111111110001
//                100000000000000000001
//                1*1*0*0*1*1*0*1*1*0*1
//                101010101011101011101
//                1*1*1*1*1*0*0*1*0*1*1
//                101010101110111101101
//                1*0*1*1*1*1*1*0*0*0*1
//                101010101010101111111
//                1*1*1*1*0*1*1*1*0*0*1
//                111010111110101110101
//                1*0*1*0*0*0*1*0*1*1*1
//                101111111111111010101
//                1*0*0*0*0*0*0*0*0*1*1
//                1111111111111111111e1
//                """;
//        boolean[][] result = new boolean[X_CELL][Y_CELL];
//        for (int i = 0; i < Y_CELL; i++) {
//            for (int j = 0; j < X_CELL; j++) {
//                char charAt = map.charAt(i * (Y_CELL + 1) + j);
//                result[i][j] = (charAt == '1');
//            }
//        }
//        return result;
//    }
//}
