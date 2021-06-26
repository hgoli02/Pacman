package sample.model;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.controller.GameController;

public class Cell extends StackPane {
    public static final int CELL_SIZE = 20;
    private final ImageView smallDot;
    private final ImageView energyBomb;
    private static final Image smallDotPicture;
    private static final Image energyBombPicture;

    private final int x;
    private final int y;
    private final boolean isWall;
    private boolean isPoint = true;
    private boolean isEnergyBomb;
    private final Rectangle rectangle;

    static {
        smallDotPicture = new Image(Cell.class.getResource("smalldot.png").toExternalForm());
        energyBombPicture = new Image(Cell.class.getResource("whitedot.png").toExternalForm());
    }

    {
        smallDot = new ImageView(smallDotPicture);
        smallDot.setFitWidth(10);
        smallDot.setFitHeight(10);
        energyBomb = new ImageView(energyBombPicture);
        energyBomb.setFitWidth(10);
        energyBomb.setFitHeight(10);
    }

    public Cell(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;

        setTranslateX(x * CELL_SIZE);
        setTranslateY(y * CELL_SIZE);

        this.setHeight(CELL_SIZE);
        this.setWidth(CELL_SIZE);
        this.setCursor(Cursor.CROSSHAIR);

        rectangle = new Rectangle(CELL_SIZE, CELL_SIZE, Color.BLACK);
        this.getChildren().add(rectangle);
        if (isWall) rectangle.setFill(Color.BLUE);

        if (!isWall && GameController.random.nextDouble() < 0.02) {
            isEnergyBomb = true;
            isPoint = false;
        }
        if (isWall) {
            isPoint = false;
            isEnergyBomb = false;
        }

        if (isPoint) {
            this.getChildren().add(smallDot);
        }

        if (isEnergyBomb) {
            this.getChildren().add(energyBomb);
        }

    }

    public boolean isPoint() {
        return isPoint;
    }

    public void setPoint(boolean point) {
        isPoint = point;
        if (!point) {
            this.getChildren().remove(smallDot);
        }
    }

    public void removeEnergyBomb() {
        if (isEnergyBomb) {
            isEnergyBomb = false;
            this.getChildren().remove(energyBomb);
        }
    }


    public boolean isWall() {
        return isWall;
    }

    public boolean isEnergyBomb() {
        return isEnergyBomb;
    }
}
