package com.example.notes;

import com.example.model.Note;

import com.jfoenix.controls.JFXTimePicker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Контроллер сущности "заметка". Реализует логику работы панели заметок на главном окне
 */
public class NoteController implements Initializable {
    /**
     * Синглтон класса NoteController
     */
    private static NoteController instance;

    /**
     * Базовый конструктор
     */
    public NoteController() {
        instance = this;
    }

    /**
     * Получает ссылку на синглтон класса NoteController
     *
     * @return Возвращает ссылку на синглтон класса NoteController
     */
    public static NoteController getInstance() {
        return instance;
    }

    @FXML
    private Button noteDeleteBtn;

    @FXML
    private Button noteEditBtn;

    @FXML
    private WebView noteText;
    private WebEngine noteEngine;

    @FXML
    private Label noteTitle;

    @FXML
    private Label noteDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Animation.ScaleButtonAnimation(noteDeleteBtn);
        Animation.ScaleButtonAnimation(noteEditBtn);
        Animation.CreateTooltip(noteDeleteBtn, "Удалить заметку");
        Animation.CreateTooltip(noteEditBtn, "Редактировать заметку");
    }

    /**
     * Поле формат даты. Используется при формировании даты создания заметки
     */
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss");

    /**
     * Заполняет информацией заметку на главном окне
     *
     * @param note заметка
     * @see Note
     */
    public void setData(Note note) {
        noteTitle.setText(note.getTitle());
        noteEngine = noteText.getEngine();
        noteEngine.loadContent(note.getText());
        noteDate.setText(dtf.format(note.getCreatedOn()));

        Animation.CreateTooltip(noteDate, dtf.format(note.getCreatedOn()));
    }

    /**
     * Вызывается после нажатия кнопки редактировать заметку
     *
     * @param mouseEvent
     */
    @FXML
    public void editNote(MouseEvent mouseEvent) {
        String noteName = noteTitle.getText();

        NoteEditController.getInstance().edit(noteName);
    }

    /**
     * Вызывается после нажатия кнопки удалить заметку
     *
     * @param mouseEvent
     * @throws IOException
     */
    @FXML
    public void deleteNote(MouseEvent mouseEvent) {
        String title = noteTitle.getText();

        NoteEditController.getInstance().delete(title);
    }

    @FXML
    public void enableNotificationNote(MouseEvent mouseEvent) {
        VBox vbox = new VBox(20);
        Scene scene = new Scene(vbox, 400, 400);

        Stage stage = new Stage();
        stage.setScene(scene);
        DatePicker startDatePicker = new DatePicker();

        startDatePicker.setValue(LocalDate.now());

        JFXTimePicker timePicker = new JFXTimePicker();
        timePicker.set24HourView(true);


        DatePicker dp = new DatePicker();
        dp.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });


        vbox.getChildren().add(new Label("Дата:"));
        vbox.getChildren().add(dp);

        //Не работает. Кидает Exception
//        vbox.getChildren().add(new Label("Время:"));
//        vbox.getChildren().add(timePicker);

        stage.show();

    }

    @FXML
    public void testNotificationMethod(MouseEvent mouseEvent) {
        LocalDateTime localDateTime = LocalDateTime.now();

        Notification notification = new Notification(localDateTime);
        notification.scheduleNotification();

    }



}
