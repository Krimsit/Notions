package com.example.notes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.dustinredmond.fxtrayicon.FXTrayIcon;

import java.io.IOException;
import java.util.Objects;

/**
 * Базовый класс, инициализирующий работу.
 * Является входной точкой в приложение
 */
public class NoteApplication extends Application {

    public static FXTrayIcon trayIcon;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        try {
            FXMLLoader.load(Objects.requireNonNull(getClass().getResource("editor.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(fxmlLoader.load(), 1440, 900);
        stage.setTitle("Notes");
        stage.setScene(scene);
        stage.getIcons().add(new Image((getClass().getResource("/com/example/img/icon.png")).toString()));
        stage.setMinHeight(600);
        stage.setMinWidth(750);
        // Pass in the app's main stage, and path to the icon image
        trayIcon = new FXTrayIcon(stage, getClass().getResource("/com/example/img/icon.png"));
        trayIcon.setApplicationTitle("Notes");
        trayIcon.show();


//        // We can also nest menus, below is an Options menu with sub-items
//        Menu menuOptions = new Menu("Options");
//        MenuItem miOn        = new MenuItem("On");
//        miOn.setOnAction(e -> System.out.println("Options -> On clicked"));
//        MenuItem miOff = new MenuItem("Off");
//        miOff.setOnAction(e -> System.out.println("Options -> Off clicked"));
//        menuOptions.getItems().addAll(miOn, miOff);
//        trayIcon.addMenuItem(menuOptions);

        trayIcon.addExitItem("Выход");

        //event listener for width property of the window
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            Controller.getInstance().resizeTilePane(newVal.doubleValue());
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}