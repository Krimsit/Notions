package com.example.notes;
import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Контроллер редактирования заметки, реализующий логику наполнения и изменения заметки
 */
public class NoteEditController {

    /**
     * Ссылка на синглтон класса NoteEditController
     */
    private static NoteEditController instance;

    /**
     * Базовый конструктор
     */
    public NoteEditController(){

        instance = this;
    }


    /**
     * Возвращает ссылку на синглтон класса NoteEditController
     */
    public static NoteEditController getInstance(){

        return instance;
    }

    /**
     * Кнопка отмены
     */
    @FXML
    private Button noteEditCancelBtn;

    /**
     * Поле заголовок
     */
    @FXML
    private TextField noteEditTitle;

    /**
     * Кнопка сохранить редактирование
     */
    @FXML
    private Button noteEditSaveBtn;

    /**
     * Текст редактируемой заметки в HTML формате
     */
    @FXML
    private HTMLEditor noteEditText;

    private static Note note;
    private static boolean editMode = false;

    /**
     * Метод переводит заметку в режим редактирования
     * @param noteToEdit редактируемая заметка
     * @see Note
     */
    public void setEditMode(Note noteToEdit){
        editMode = true;
        note = noteToEdit;
        System.out.println(note.getId());
        System.out.println(note.getTitle());
        System.out.println(note.getText());
        noteEditTitle.setText(note.getTitle());
        noteEditText.setHtmlText(note.getText());
    }

    /**
     * Вызывается при нажатии на кнопку отмены редактирования
     */
    @FXML
    private void noteEditCancelBtnClicked(){
        Controller.getInstance().noteEditHide();
    }

    /**
     * Вызывается при нажатии на кнопку сохранить изменения
     * @throws IOException
     */
    @FXML
    private void noteEditSaveBtnClicked() throws IOException {
        if (!editMode) {
            Note note = new Note();
            note.setId(Controller.getInstance().getLastId() + 1);
            note.setText(noteEditText.getHtmlText());
            note.setTitle(noteEditTitle.getText());
            LocalDateTime now = LocalDateTime.now();
            note.setCreatedOn(now);
            if (Controller.getInstance().checkNonUniqueName(note.getTitle())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.show();
            } else {
                Controller.getInstance().noteAdd("NotesStored", note);
                Controller.getInstance().noteEditHide();
            }
        }
        else{
            String prevNoteTitle = note.getTitle();
            note.setText(noteEditText.getHtmlText());
            note.setTitle(noteEditTitle.getText());
            System.out.println("---"+prevNoteTitle+"----"+note.getTitle());
            LocalDateTime now = LocalDateTime.now();
            note.setCreatedOn(now);
            if (prevNoteTitle.equals(note.getTitle())) {
                Controller.getInstance().noteDelete(prevNoteTitle);
                Controller.getInstance().noteAdd("NotesStored", note);

                Controller.getInstance().noteEditHide();
            }
            else if (Controller.getInstance().checkNonUniqueName(note.getTitle())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.show();
            } else {
                Controller.getInstance().noteDelete(prevNoteTitle);
                Controller.getInstance().noteAdd("NotesStored", note);

                Controller.getInstance().noteEditHide();
            }
            editMode = false;
        }
    }
}
