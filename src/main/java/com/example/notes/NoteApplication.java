package com.example.notes;

import com.example.model.Note;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.Integer.parseInt;

/**
 * Базовый класс, инициализирующий работу.
 * Является входной точкой в приложение
 */
public class NoteApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1440, 900);
        stage.setTitle("Notes");
        stage.setScene(scene);

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            Controller.getInstance().resizeTilePane(newVal.doubleValue());
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}