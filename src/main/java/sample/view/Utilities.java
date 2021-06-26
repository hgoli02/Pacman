package sample.view;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.controller.GameController;
import sample.model.Cell;

import java.util.Arrays;
import java.util.Random;

import static sample.controller.GameController.X_CELL;
import static sample.view.GameView.Y_CELL;

public class Utilities {
    static String createMaze(int maximumCoordinatesOfX, int maximumCoordinatesOfY) {
        char[][] map = new char[2 * maximumCoordinatesOfY + 1][2 * maximumCoordinatesOfX + 1];
        initializeArray(map, maximumCoordinatesOfY, maximumCoordinatesOfX);
        dfs(1, 1, map, maximumCoordinatesOfX, maximumCoordinatesOfY);
        mapToStar(map, maximumCoordinatesOfY, maximumCoordinatesOfX);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if ((GameController.random.nextDouble() < 0.3) && j > 0 && j < 20 && i > 0 && i < 20 )
                    result.append("0");
                else
                result.append(map[i][j]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static VBox createGrid(String mapString) {
        boolean[][] map = createMap(mapString);
        GridPane grid = new GridPane();
        for (int i = 0; i < Y_CELL; i++){
            for (int j = 0; j < X_CELL; j++){
                grid.add(new Rectangle(10,10,!map[i][j] ? Color.BLACK : Color.BLUE),j,i);
            }
        }
        return new VBox(grid);
    }

    public static boolean[][] createMap(String map) {
        boolean[][] result = new boolean[X_CELL][Y_CELL];
        for (int i = 0; i < Y_CELL; i++) {
            for (int j = 0; j < X_CELL; j++) {
                char charAt = map.charAt(i * (Y_CELL + 1) + j);
                result[i][j] = (charAt == '1');
            }
        }
        result[10][10] = false;
        result[9][9] = false;
        result[10][9] =false;
        return result;
    }


    static void mapToStar(char[][] map, int maximumCoordinatesOfY, int maximumCoordinatesOfX) {
        for (int i = 1; i <= maximumCoordinatesOfY; i++) {
            for (int j = 1; j <= maximumCoordinatesOfX; j++) {
                map[2 * i - 1][2 * j - 1] = '*';
            }
        }
        map[0][1] = 'e';
        map[maximumCoordinatesOfY * 2][maximumCoordinatesOfX * 2 - 1] = 'e';
    }

    static void dfs(int x, int y, char[][] map, int maximumCoordinatesOfX, int maximumCoordinatesOfY) {
        map[y][x] = '0';
        Random random = new Random();
        int move = random.nextInt(4) + 1;
        for (int i = 0; i < 4; i++) {
            move(move % 4 + 1, x, y, maximumCoordinatesOfX, maximumCoordinatesOfY, map);
            move++;
        }
    }

    static boolean isSafe(int x, int y, int maximumX, int maximumY, char[][] map) {
        if (x > 0 && x < 2 * maximumX) {
            if (y > 0 && y < 2 * maximumY) {
                return map[y][x] == '1' && map[y][x] != '*';
            }
        }
        return false;
    }

    static void initializeArray(char[][] array, int yAxis, int xAxis) {
        for (int i = 0; i < 2 * yAxis + 1; i++) {
            for (int j = 0; j < 2 * xAxis + 1; j++) {
                array[i][j] = '1';
            }
        }
    }

    static void move(int move, int x, int y, int maximumCoordinatesOfX, int maximumCoordinatesOfY, char[][] map) {
        if (move == 1) {
            if (isSafe(x + 2, y, maximumCoordinatesOfX, maximumCoordinatesOfY, map)) {
                map[y][x + 1] = '0';
                map[y][x + 2] = '0';
                dfs(x + 2, y, map, maximumCoordinatesOfX, maximumCoordinatesOfY);
            }
        } else if (move == 2) {
            if (isSafe(x, y + 2, maximumCoordinatesOfX, maximumCoordinatesOfY, map)) {
                map[y + 1][x] = '0';
                map[y + 2][x] = '0';
                dfs(x, y + 2, map, maximumCoordinatesOfX, maximumCoordinatesOfY);
            }
        } else if (move == 3) {
            if (isSafe(x - 2, y, maximumCoordinatesOfX, maximumCoordinatesOfY, map)) {
                map[y][x - 1] = '0';
                map[y][x - 2] = '0';
                dfs(x - 2, y, map, maximumCoordinatesOfX, maximumCoordinatesOfY);
            }
        } else if (move == 4) {
            if (isSafe(x, y - 2, maximumCoordinatesOfX, maximumCoordinatesOfY, map)) {
                map[y - 1][x] = '0';
                map[y - 2][x] = '0';
                dfs(x, y - 2, map, maximumCoordinatesOfX, maximumCoordinatesOfY);
            }
        }
    }
}
