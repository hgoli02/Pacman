package sample.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Pacman extends Rectangle {
    Image image = new Image(Objects.requireNonNull(getClass().getResource("/sample/model/pacmanDown.gif")).toExternalForm());
    private Direction direction = Direction.DOWN;
    private int x = 9;
    private int y = 9;

    public Pacman() {
        this.setWidth(Cell.CELL_SIZE);
        this.setHeight(Cell.CELL_SIZE);
        this.setTranslateX(Cell.CELL_SIZE);
        this.setFill(new ImagePattern(image));
        this.setTranslateX(9 * Cell.CELL_SIZE);
        this.setTranslateY(9 * Cell.CELL_SIZE);
    }


    public void changeY(int amount) {
        y += amount;
        double current = getTranslateY();
        int difference = amount > 0 ? Cell.CELL_SIZE : -Cell.CELL_SIZE;
        setTranslateY(current + difference);
    }

    public void changeX(int amount) {
        x += amount;
        double current = getTranslateX();
        int difference = amount > 0 ? Cell.CELL_SIZE : -Cell.CELL_SIZE;
        setTranslateX(current + difference);
    }

    public boolean canMove(Cell[][] map) {
        try {
            if (direction == Direction.RIGHT) {
                return !map[y][x + 1].isWall();
            } else if (direction == Direction.LEFT) {
                return !map[y][x - 1].isWall();
            } else if (direction == Direction.DOWN) {
                return !map[y + 1][x].isWall();
            } else
                return !map[y - 1][x].isWall();
        } catch (Exception e) {
            return false;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        if (this.direction == direction)
            return;
        image = new Image(Resources.getResourceByDirection(direction));
        this.setFill(new ImagePattern(image));
        this.direction = direction;
    }

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    public int getXCoordinate() {
        return x;
    }

    public int getYCoordinate() {
        return y;
    }

    public void resetPosition() {
        x = 9;
        y = 9;
        setTranslateX(Cell.CELL_SIZE * 9);
        setTranslateY(Cell.CELL_SIZE * 9);
    }

    public enum Direction {UP, DOWN, LEFT, RIGHT}
}
