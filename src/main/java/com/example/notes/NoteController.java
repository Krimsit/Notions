package com.example.notes;

import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Контроллер сущности "заметка", реализующий логику работы панели заметок на главном окне
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
    public void testMethod(MouseEvent mouseEvent) throws GeneralSecurityException, IOException {
        GoogleCalendarApi googleCalendarApi = new GoogleCalendarApi();
        googleCalendarApi.createEvent();
    }



}
