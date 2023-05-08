package utils;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXMLUtils {
    public static void alertDialog(String message, String header, String setContentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(header);
        alert.setHeaderText(message);
        alert.setContentText(setContentText);

        alert.showAndWait();
    }


    public static <T> void updateItem(TableView<T> listView, T item, String title, VBox content, Runnable callback) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Button okButton = new Button("OK");
        okButton.setDefaultButton(true);
        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);

        okButton.setOnAction(e -> {
            callback.run();
            stage.close();
        });
        cancelButton.setOnAction(e -> stage.close());

        HBox buttonBox = new HBox(okButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(okButton, Priority.ALWAYS);
        HBox.setHgrow(cancelButton, Priority.ALWAYS);

        VBox vbox = new VBox(content, buttonBox);
        vbox.setSpacing(10);
        vbox.setPadding(new javafx.geometry.Insets(10));

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.showAndWait();
        Platform.runLater(listView::requestFocus);
    }

    public static void throwAlert(String pageName, String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(pageName);
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }
}



