package sample.model;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.controller.GameController;

import java.util.Objects;

public class Ghost extends Rectangle {
    private boolean isActive = true;
    Image image;
    private int xTarget = 1;
    private int yTarget = 1;
    private int x;
    private int y;
    private final GhostColor ghostColor;
    private final Cell[][] map;
    private final boolean[][] visited = new boolean[GameController.Y_CELL][GameController.X_CELL];
    private final boolean[][] currentPath = new boolean[GameController.Y_CELL][GameController.X_CELL];

    public Ghost(GhostColor ghostColor, int x, int y, Cell[][] map) {
        this.x = x;
        this.y = y;
        this.map = map;
        this.setWidth(Cell.CELL_SIZE);
        this.setHeight(Cell.CELL_SIZE);
        this.ghostColor = ghostColor;
        this.setTranslateX((x) * Cell.CELL_SIZE);
        this.setTranslateY((y) * Cell.CELL_SIZE);
        image = new Image(Objects.requireNonNull(getClass().getResource(ghostColor + ".gif")).toExternalForm());
        this.setFill(new ImagePattern(image));
    }

    public void setPacmanX(int pacmanX) {
        this.xTarget = pacmanX;
    }

    public void setPacmanY(int pacmanY) {
        this.yTarget = pacmanY;
    }

    public void updateGhostTarget(Pacman pacman) {
        setPacmanY(pacman.getYCoordinate());
        setPacmanX(pacman.getXCoordinate());
        resetVisited();
        resetVisited2();
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void moveGhost(Pacman pacman) {
        if (GameController.isCrazyMode()) {
            moveOnRandom();
            return;
        }
        if (GameController.getInstance().isHard()) {
            moveDfs(pacman);
        } else {
            moveOnRandom();
        }
    }

    public void moveDfs(Pacman pacman) {
        resetVisited();
        if (x == xTarget && y == yTarget) {
            updateGhostTarget(pacman);
        }
        currentPath[y][x] = true;
        if (y == yTarget && x == xTarget) {
            return;
        }
        int index = GameController.random.nextInt(4) + 1;
        for (int k = index; k < index + 4; k++) {
            int i = k % 4;
            if (i == 1) {
                if (willReachViaDfs(x - 1, y)) {
                    changeX(-1);
                    return;
                }
            }
            if (i == 2) {
                if (willReachViaDfs(x, y - 1)) {
                    changeY(-1);
                    return;
                }
            }
            if (i == 3) {
                if (willReachViaDfs(x + 1, y)) {
                    changeX(+1);
                    return;
                }
            }
            if (i == 0) {
                if (willReachViaDfs(x, y + 1)) {
                    changeY(+1);
                    return;
                }
            }
        }
    }

    public void moveOnRandom() {
        int choice = GameController.random.nextInt(4);
        for (int i = choice; i < choice + 4; i++) {
            int move = i % 4;
            switch (move) {
                case 0:
                    if (!map[y][x + 1].isWall() && isInBound(x + 1, y)) {
                        changeX(+1);
                        return;
                    }
                    break;
                case 3:
                    if (!map[y][x - 1].isWall() && isInBound(x - 1, y)) {
                        changeX(-1);
                        return;
                    }
                    break;
                case 1:
                    if (!map[y + 1][x].isWall() && isInBound(x - 1, y + 1)) {
                        changeY(+1);
                        return;
                    }
                    break;
                case 2:
                    if (!map[y - 1][x].isWall() && isInBound(x , y - 1)) {
                        changeY(-1);
                        return;
                    }
            }
        }
    }

    private boolean isInBound(int x, int y) {
        return x >= 0 && x < GameController.X_CELL && y >= 0 && y < GameController.Y_CELL;
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

    public int getXCoordinate() {
        return x;
    }

    public int getYCoordinate() {
        return y;
    }

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

    private boolean willReachViaDfs(int x, int y) {
        if (y >= GameController.Y_CELL || y <= 0 || x >= GameController.X_CELL || x <= 0) {
            return false;
        }
        if (map[y][x].isWall() || visited[y][x] || currentPath[y][x]) {
            return false;
        }
        if (x == xTarget && y == yTarget) {
            return true;
        }
        visited[y][x] = true;
        boolean willReach = willReachViaDfs(x - 1, y) ||
                willReachViaDfs(x, y - 1) ||
                willReachViaDfs(x + 1, y) ||
                willReachViaDfs(x, y + 1);
        //visited[y][x] = false;
        return willReach;

    }

    private void drawMap(int currx, int curry) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                if (i == curry && j == currx) {
                    result.append("$");
                } else if (visited[i][j]) {
                    result.append("@");
                } else {
                    if (map[i][j].isWall()) {
                        result.append("#");
                    } else
                        result.append("O");
                }
            }
            result.append("\n");
        }
        result.append("**************************\n");
        System.out.println(result);
    }

    public void resetVisited() {
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[0].length; j++) {
                visited[i][j] = false;
            }
        }
        visited[y][x] = false;
    }

    public void resetVisited2() {
        for (int i = 0; i < currentPath.length; i++) {
            for (int j = 0; j < currentPath[0].length; j++) {
                currentPath[i][j] = false;
            }
        }
        currentPath[y][x] = false;
    }

    public void setGhostMode() {
        GameController.setIsCrazy(true);
        this.setFill(new ImagePattern(new Image(getClass().getResource("blueghost.gif").toExternalForm())));
    }

    public void resetGhostMode() {
        resetVisited2();
        resetVisited();
        GameController.setIsCrazy(false);
        this.setFill(new ImagePattern(new Image(getClass().getResource(ghostColor + ".gif").toExternalForm())));
    }

    public void resetPosition() {
        resetVisited();
        resetVisited2();
        switch (ghostColor) {
            case RED -> setPosition(1, 1);
            case BLUE -> setPosition(19, 19);
            case YELLOW -> setPosition(1, 19);
            case PINKY -> setPosition(19, 1);
        }

    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        setTranslateY(y * Cell.CELL_SIZE);
        setTranslateX(x * Cell.CELL_SIZE);
    }

    public enum GhostColor {PINKY, RED, YELLOW, BLUE}

}
