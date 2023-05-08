package fxControllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import model.Driver;
import model.Manager;
import model.User;
import utils.DbUtils;
import utils.FXMLUtils;
import utils.LoginPreferences;

import java.io.IOException;
import java.sql.SQLException;


public class LoginPage {


    public TextField loginField;
    public PasswordField passwordField;
    ObservableList <Driver> listD = FXCollections.observableArrayList();
    ObservableList <Manager> listM = FXCollections.observableArrayList();

    public void login() throws IOException, SQLException {

        User user = DbUtils.validateUser(loginField.getText(), passwordField.getText());
        if (user != null) {
            LoginPreferences.putValue("login", loginField.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/main-page.fxml"));
            Parent parent = fxmlLoader.load();

            MainPage mainPage = fxmlLoader.getController();
            if (user instanceof Driver) {
                mainPage.managerListField.setVisible(false);
            } else if (user instanceof Manager){
                mainPage.driverListField.setItems(DbUtils.getDataDriver(null));
            }
            mainPage.setInfo(user);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("FreightSys");
            stage.setScene(scene);
            stage.show();
        } else {
            FXMLUtils.alertDialog("No such user", "User error", "Sign up or try again");
        }
    }

    public void register() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginPage.class.getResource("../view/registration-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setTitle("FreightSys");
        stage.setScene(scene);
        stage.show();
    }
}
