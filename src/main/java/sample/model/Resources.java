package sample.model;

public enum Resources {
    PACMAN_UP("/sample/model/pacmanUp.gif"),
    PACMAN_DOWN("/sample/model/pacmanDown.gif"),
    PACMAN_RIGHT("/sample/model/pacmanRight.gif"),
    PACMAN_LEFT("/sample/model/pacmanLeft.gif");

    String value;

    Resources(String value) {
        this.value = value;
    }

    public static String getResourceByDirection(Pacman.Direction direction) {
        if (direction == Pacman.Direction.DOWN) {
            return PACMAN_DOWN.value;
        } else if (direction == Pacman.Direction.UP) {
            return PACMAN_UP.value;
        } else if (direction == Pacman.Direction.RIGHT)
            return PACMAN_RIGHT.value;
        else
            return PACMAN_LEFT.value;
    }
}
