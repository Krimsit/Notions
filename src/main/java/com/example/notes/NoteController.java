package com.example.notes;

import com.example.notes.Controller;
import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

/**
 * Контроллер сущности "заметка", реализующий логику работы панели заметок на главном окне
 */
public class NoteController implements Initializable {
    /**
     * Поле панели "заметка". Является синглтоном класса NoteController
     */
    private static NoteController instance;

    /**
     * Базовый конструктор
     */
    public NoteController(){
        instance = this;
    }
    /**
     * Получить ссылку на синглтон класса NoteController
     * @return Возвращает ссылку на синглтон класса NoteController
     */
    public static NoteController getInstance(){
        return instance;
    }

    @FXML
    private VBox noteContainer;
    @FXML
    private Label noteId;

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
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");

    /**
     * Заполняет информацией заметку на главном окне
     * @param note заметка
     * @see Note
     */
    public void setData(Note note){
        noteId.setText(note.getId().toString());
        noteTitle.setText(note.getTitle());
        noteEngine = noteText.getEngine();
        noteEngine.loadContent(note.getText());
        noteDate.setText(dtf.format(note.getCreatedOn()));
    }

    /**
     * Вызывается после нажатия кнопки редактировать заметку
     * @param mouseEvent
     */
    @FXML
    public void editNoteBtnClicked(MouseEvent mouseEvent) {
        String title = noteTitle.getText();
        Controller.getInstance().noteEdit("NotesStored", title);
    }

    /**
     * Вызывается после нажатия кнопки удалить заметку
     * @param mouseEvent
     * @throws IOException
     */
    @FXML
    public void deleteNoteBtnClicked(MouseEvent mouseEvent) throws IOException {
        String title = noteTitle.getText();
        Controller.getInstance().noteDelete(title);
    }

    /**
     * Изменяет размер окна заметки
     * @param windowWidth
     */
    public void resizeNote(double windowWidth){
    noteContainer.setPrefWidth((windowWidth-244)/3);
    }
}
