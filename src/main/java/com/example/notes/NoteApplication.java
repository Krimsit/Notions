package com.example.notes;

import com.example.model.Note;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.Integer.parseInt;

public class NoteApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NoteApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1078, 700);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        //event listener for width property of the window
        //stage.widthProperty().addListener((obs, oldVal, newVal) -> {
          //  NoteController.getInstance().resizeNote(newVal.doubleValue());
        //});
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}