package sample.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sample.model.Cell;
import sample.model.Ghost;
import sample.model.Pacman;
import sample.model.User;
import sample.view.GameView;
import sample.view.View;
import sample.view.ViewSwitcher;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    public static final int X_CELL = 21;
    public static final int Y_CELL = 21;
    public static Random random = new Random();
    private static GameController instance;
    private static boolean isCrazy = false;
    private static MediaPlayer scorePlayer;
    private static MediaPlayer backGroundPlayer;
    Pacman pacman;
    private boolean isHard = true;
    private String map = DatabaseController.getMap("beginning");
    private int lifePoints = 2;
    private int life = 2;
    private int numberOfPoints;
    private int scorePoint;
    private Cell[][] grid;
    private int crazyCounter = 0;
    private ArrayList<Ghost> ghosts;
    private GameView gameView;
    private boolean isMute;
    public boolean isPaused = false;

    public static GameController getInstance() {
        if (instance == null) instance = new GameController();
        return instance;
    }

    public static boolean isCrazyMode() {
        return isCrazy;
    }

    public static void setIsCrazy(boolean isCrazy) {
        GameController.isCrazy = isCrazy;
    }

    public int getLife() {
        return lifePoints;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public boolean isHard() {
        return isHard;
    }

    public void setHard(boolean hard) {
        isHard = hard;
    }

    public void movePacman(KeyCode keyCode) {
        switch (keyCode) {
            case W -> pacman.setDirection(Pacman.Direction.UP);
            case D -> pacman.setDirection(Pacman.Direction.RIGHT);
            case A -> pacman.setDirection(Pacman.Direction.LEFT);
            case S -> pacman.setDirection(Pacman.Direction.DOWN);
            case M -> {
                backGroundPlayer.muteProperty().setValue(!backGroundPlayer.muteProperty().get());
                isMute = !isMute;
            }
            case P->{
                isPaused = !isPaused;
            }
        }
    }

    public void createGrid(Pane pane) {
        boolean[][] map = createMap();
        grid = new Cell[Y_CELL][X_CELL];
        for (int i = 0; i < Y_CELL; i++) {
            for (int j = 0; j < X_CELL; j++) {
                Cell cell = new Cell(j, i, map[i][j]);
                grid[i][j] = cell;
                if (cell.isPoint()) {
                    numberOfPoints++;
                }
                pane.getChildren().add(cell);
            }
        }
    }

    public boolean[][] createMap() {
        String map = getMap();
        if(map == null) map = DatabaseController.getMap("beginning");
        map = map.replaceAll("\r", "");
        boolean[][] result = new boolean[X_CELL][Y_CELL];
        for (int i = 0; i < Y_CELL; i++) {
            for (int j = 0; j < X_CELL; j++) {
                char charAt = map.charAt(i * (Y_CELL + 1) + j);
                result[i][j] = (charAt == '1');
            }
        }
        return result;
    }

    public void initializeMusic() {
        String path = getClass().getResource("/sample/background.mp3").toString();
        Media media = new Media(path);
        backGroundPlayer = new MediaPlayer(media);
        backGroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backGroundPlayer.autoPlayProperty().setValue(true);
        backGroundPlayer.setOnEndOfMedia(backGroundPlayer::play);
        backGroundPlayer.play();
    }

    private void playScoreMusic() {
        if (isMute) {
            return;
        }
        if (scorePlayer == null) {
            String path = getClass().getResource("/sample/pacman_eatfruit.wav").toString();
            Media media = new Media(path);
            scorePlayer = new MediaPlayer(media);
        }
        scorePlayer.setOnEndOfMedia(scorePlayer::stop);
        scorePlayer.play();
    }

    private void playGhostEat() {
        if (isMute) {
            return;
        }
        String path = getClass().getResource("/sample/pacman_eatghost.wav").toString();
        Media media = new Media(path);
        MediaPlayer player = new MediaPlayer(media);
        player.setOnEndOfMedia(player::stop);
        player.play();
    }

    private void playDeath() {
        if (isMute) {
            return;
        }
        String path = getClass().getResource("/sample/pacman_death.wav").toString();
        Media media = new Media(path);
        MediaPlayer player = new MediaPlayer(media);
        player.setOnEndOfMedia(player::stop);
        player.play();
    }

    public void initGhosts(Pane pane) {
        ghosts = new ArrayList<>();
        pacman = new Pacman();
        Ghost orange = new Ghost(Ghost.GhostColor.YELLOW, (X_CELL - 2), Y_CELL - 1, grid);
        Ghost red = new Ghost(Ghost.GhostColor.RED, X_CELL - 1, 2, grid);
        Ghost blue = new Ghost(Ghost.GhostColor.BLUE, 1, Y_CELL - 2, grid);
        Ghost pinky = new Ghost(Ghost.GhostColor.PINKY,1,1,grid);
        ghosts.add(orange);
        ghosts.add(pinky);
        ghosts.add(red);
        ghosts.add(blue);
        pane.getChildren().add(pacman);
        for (Ghost aGhost : ghosts) {
            pane.getChildren().add(aGhost);
        }
    }

    public void moveGhost() {
        for (Ghost ghost : ghosts) {
            if (ghost.isActive()) {
                ghost.moveGhost(pacman);
            }
        }
    }

    public void movePacmanFrame() {
        if (pacman.canMove(grid)) {
            switch (pacman.getDirection()) {
                case UP -> pacman.changeY(-1);
                case DOWN -> pacman.changeY(+1);
                case LEFT -> pacman.changeX(-1);
                case RIGHT -> pacman.changeX(+1);
            }
            System.out.println(pacman.toString());
        }
    }

    public void updateGhostTarget() {
        for (Ghost ghost : ghosts) {
            ghost.updateGhostTarget(pacman);
        }
    }

    public void handleFrames() {
        if (grid[pacman.getYCoordinate()][pacman.getXCoordinate()].isPoint()) {
            numberOfPoints--;
            changeScore();
            playScoreMusic();
            grid[pacman.getYCoordinate()][pacman.getXCoordinate()].setPoint(false);
        } else if (grid[pacman.getYCoordinate()][pacman.getXCoordinate()].isEnergyBomb()) {
            changeScore();
            grid[pacman.getYCoordinate()][pacman.getXCoordinate()].removeEnergyBomb();
            for (Ghost ghost : ghosts) {
                ghost.setGhostMode();
            }
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            for (Ghost ghost : ghosts) {
                                ghost.resetGhostMode();
                            }
                            crazyCounter = 0;
                        }
                    },
                    10000
            );

        }
        if (numberOfPoints == 0) {
            System.out.println("WIN");
            new Alert(Alert.AlertType.CONFIRMATION, "Congrats you won");
            backGroundPlayer.stop();
            LoginController.getInstance().getOnlineUser().increaseScore(scorePoint);
            DatabaseController.updateUser(LoginController.getInstance().getOnlineUser());
            gameView.parallelTransition.stop();
            gameView.animationTimer.stop();
            ViewSwitcher.switchTo(View.MAIN_MENU);
        }
        for (Ghost ghost : ghosts) {
            if (pacman.getXCoordinate() == ghost.getXCoordinate() &&
                    pacman.getYCoordinate() == ghost.getYCoordinate()) {
                if (isCrazy) {
                    eatGhost(ghost);
                    playGhostEat();
                } else {
                    finishGame();
                }
            }
        }
    }

    private void changeScore() {
        scorePoint += 5;
        Label score = gameView.score;
        score.setText(String.format("%d", Integer.parseInt(score.getText()) + 5));
    }

    public void setView(GameView gameView) {
        this.gameView = gameView;
    }

    private void eatGhost(Ghost ghost) {
        crazyCounter++;
        updateCrazyCounterScore();
        ghost.setActive(false);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        ghost.setActive(true);
                    }
                },
                4000
        );
        ghost.resetPosition();
    }

    private void updateCrazyCounterScore() {
        int points = crazyCounter * 200;
        Label score = gameView.score;
        scorePoint += points;
        score.setText(String.format("%d", Integer.parseInt(score.getText()) + points));
    }

    private void finishGame() {
        lifePoints--;
        if (lifePoints == 0) {
            User onlineUser = LoginController.getInstance().getOnlineUser();
            onlineUser.increaseScore(scorePoint);
            DatabaseController.updateUser(onlineUser);
            backGroundPlayer.stop();
            gameView.parallelTransition.stop();
            gameView.animationTimer.stop();
            ViewSwitcher.switchTo(View.MAIN_MENU);
            return;
        }
        playDeath();
        Label life = gameView.life;
        for (Ghost ghost : ghosts) {
            ghost.resetVisited2();
            ghost.resetVisited();
        }
        life.setText(String.valueOf(lifePoints));
        pacman.resetPosition();
    }

    public void init() {
        this.lifePoints = life;
    }
}
