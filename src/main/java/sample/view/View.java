package sample.view;

public enum View {
    WELCOME("login.fxml"),
    MAIN_MENU("main.fxml"),
    SETTING("setting.fxml"),
    GAME(""),
    ScoreBoard("");

    private final String file;

    View(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
