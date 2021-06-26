package sample.view;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class MainMenu {
    public void settingTapped(MouseEvent mouseEvent) {
        ViewSwitcher.switchTo(View.SETTING);
    }

    public void newGame(MouseEvent mouseEvent) throws IOException {
        ViewSwitcher.switchTo(View.GAME);
//        Game game = new Game();
//        game.setLifePoints(GameController.getInstance().getLife());
//        game.setIsHard(GameController.getInstance().isHard());
//        game.start(ViewSwitcher.getStage());
        GameView gameView = new GameView();
        gameView.start(ViewSwitcher.getStage());
    }

    public void scoreBoard(MouseEvent mouseEvent) {
        ViewSwitcher.switchTo(View.ScoreBoard);
    }

    public void logout(MouseEvent mouseEvent) {
        ViewSwitcher.switchTo(View.WELCOME);
    }
}
