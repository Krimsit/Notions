package com.example.notes;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Вспомогательный класс, отвечающий за логирование ошибок на компьютер
 */
public class Logger {


    /**
     * Метод записывает Exception StackTrace на компьютер пользователя
     * @param ex
     */
    public static void writeException(Exception ex){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uu-HH.mm.ss");

        /**
         * Директрория хранения логов. По умолчанию logs/
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
            showAlertMessage(ex.toString());
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
     * Вызывет сообщение об ошибке
     * @param messageError текст ошибки
     */
    public static void showAlertMessage(String messageError){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Ошибка!");
        alert.setContentText(messageError + "\nПроверьте логи");
        alert.setHeaderText("Внимание!");

        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image((Logger.class.getResource("/com/example/img/icon.png")).toString()));

        alert.show();

    }

}
