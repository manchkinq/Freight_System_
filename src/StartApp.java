
import com.mysql.cj.log.Log;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DbUtils;
import utils.LoginPreferences;


import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.BackingStoreException;

public class StartApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartApp.class.getResource("view/login-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("FreightSys");
        stage.setScene(scene);
        stage.show();
    }
        public static void main(String[] args) throws BackingStoreException, IOException, SQLException, ClassNotFoundException {
            launch();
            LoginPreferences.clearKey();
        }

    }
