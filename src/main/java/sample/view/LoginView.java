package sample.view;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.controller.LoginController;

public class LoginView extends Application {

    public PasswordField password;
    public TextField username;

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        ViewSwitcher.setStage(stage);
        ViewSwitcher.switchTo(View.WELCOME);
        stage.show();
    }


    public void signUp() {
        try {
            LoginController.getInstance().signUp(username.getText(), password.getText());
            new Alert(Alert.AlertType.INFORMATION, "Sign up successful").show();
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(exception.getMessage());
            alert.show();
        }

    }

    public void login() {
        try {
            LoginController.getInstance().login(username.getText(), password.getText());
            new Alert(Alert.AlertType.INFORMATION, "Login Successful").show();
            ViewSwitcher.switchTo(View.MAIN_MENU);
        } catch (Exception exception) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(exception.getMessage());
            alert.show();
        }


    }

    public void changePassword() {
        try {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Enter you username");
            inputDialog.setContentText("username");
            inputDialog.setTitle("forgetPassword");
            inputDialog.showAndWait();
            String username = inputDialog.getEditor().getText();
            inputDialog.setContentText("old password");
            inputDialog.getEditor().clear();
            inputDialog.showAndWait();
            String oldPassword = inputDialog.getEditor().getText();
            inputDialog.setContentText("newPassword");
            inputDialog.getEditor().clear();
            inputDialog.showAndWait();
            String newPassword = inputDialog.getEditor().getText();
            LoginController.getInstance().changePassword(username, newPassword, oldPassword);
            new Alert(Alert.AlertType.INFORMATION, "Password changed successfully").show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    public void removeAccount() {
        try {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setHeaderText("Enter you username");
            inputDialog.setContentText("username");
            inputDialog.setTitle("remove account");
            inputDialog.showAndWait();
            String username = inputDialog.getEditor().getText();
            inputDialog.setContentText("password");
            inputDialog.getEditor().clear();
            inputDialog.showAndWait();
            String password = inputDialog.getEditor().getText();
            LoginController.getInstance().removeAccount(username, password);
            new Alert(Alert.AlertType.INFORMATION, "user deleted successfully").show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(e.getMessage());
            alert.show();
        }

    }
}
