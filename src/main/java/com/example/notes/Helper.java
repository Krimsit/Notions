package com.example.notes;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.Map;


/**
 * Вспомогательный класс хелпер
 */
public class Helper {
    private static MFXGenericDialog dialogContent;
    private static MFXStageDialog dialog;

    /**
     * Перечисление категории звуков. ERROR - ошибка. NOTIFICATION - уведомление. EVENT - событие.
     */
    public enum SoundType {
        ERROR, NOTIFICATION, EVENT
    }

    /**
     * Метод записывает Exception StackTrace на компьютер пользователя
     *
     * @param ex
     */
    public static void writeException(Exception ex) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uu-HH.mm.ss");

        /**
         * Директория хранения логов. По умолчанию logs/
         */
        File logDirectory = new File("logs/");

        if (!logDirectory.exists())
            logDirectory.mkdirs();

        File log = new File(logDirectory.getPath() + "/" + "log-" + dtf.format(LocalDateTime.now()) + ".log");

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(log);

            if (fileWriter != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                String sStackTrace = sw.toString();
                fileWriter.write(sStackTrace);

            }
            //showAlertMessage(ex.toString());
            showErrorDialog(ex.toString() + "\nПроверьте журнал ошибок в папке logs");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Вызывает сообщение об ошибке
     *
     * @param messageError текст ошибки
     */
    public static void showAlertMessage(String messageError) {
        playSound(SoundType.ERROR);

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Ошибка!");
        alert.setContentText(messageError + "\nПроверьте журнал ошибок в папке logs");
        alert.setHeaderText("Внимание!");

        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image((Helper.class.getResource("/com/example/img/icon.png")).toString()));

        alert.show();
    }

    /**
     * Показывает модальное окно с ошибкой
     *
     * @param messageError содержание ошибки
     */
    public static void showErrorDialog(String messageError) {
        dialogContent = MFXGenericDialogBuilder.build()
                .setHeaderText("Внимание!")
                .setHeaderIcon(new MFXFontIcon("mfx-exclamation-circle-filled", 18))
                .setContentText(messageError)
                .get();

        dialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .setDraggable(true)
                .setTitle("Loading...")
                .get();

        dialogContent.addActions(
                Map.entry(new MFXButton("Назад"), event -> dialog.close())
        );
        playSound(SoundType.ERROR);
        dialog.showDialog();
    }

    /**
     * Запускает звук
     *
     * @param soundType тип звука
     */
    public static void playSound(SoundType soundType) {
        switch (soundType) {
            case ERROR:
                String errorSound = "Error.wav";
                Media errorMedia = new Media(Helper.class.getResource("/com/example/sounds/" + errorSound).toExternalForm());
                MediaPlayer errorMediaPlayer = new MediaPlayer(errorMedia);
                errorMediaPlayer.play();
                break;
            case NOTIFICATION:
                String notificationSound = "Notification.wav";

                Media notificationMedia = new Media(Helper.class.getResource("/com/example/sounds/" + notificationSound).toExternalForm());
                MediaPlayer notificationMediaPlayer = new MediaPlayer(notificationMedia);
                notificationMediaPlayer.play();
                break;
            case EVENT:
                String eventSound = "Event.wav";

                Media eventMedia = new Media(Helper.class.getResource("/com/example/sounds/" + eventSound).toExternalForm());
                MediaPlayer eventMediaPlayer = new MediaPlayer(eventMedia);
                eventMediaPlayer.play();
                break;
        }
    }
}
