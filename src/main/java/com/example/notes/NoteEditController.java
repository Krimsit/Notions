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

public class NoteEditController {

    private static NoteEditController instance;

    public NoteEditController(){
        instance = this;
    }
    public static NoteEditController getInstance(){
        return instance;
    }

    @FXML
    private Button noteEditCancelBtn;

    @FXML
    private TextField noteEditTitle;

    @FXML
    private Button noteEditSaveBtn;

    @FXML
    private HTMLEditor noteEditText;

    private static Note note;
    private static boolean editMode = false;
    public void setEditMode(Note noteToEdit){
        editMode = true;
        note = noteToEdit;
        System.out.println(note.getId());
        System.out.println(note.getTitle());
        System.out.println(note.getText());
        noteEditTitle.setText(note.getTitle());
        noteEditText.setHtmlText(note.getText());
    }
    @FXML
    private void noteEditCancelBtnClicked(){
        Controller.getInstance().noteEditHide();
    }
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
                Controller.getInstance().noteAdd(note);
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
                Controller.getInstance().noteAdd(note);

                Controller.getInstance().noteEditHide();
            }
            else if (Controller.getInstance().checkNonUniqueName(note.getTitle())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.show();
            } else {
                Controller.getInstance().noteDelete(prevNoteTitle);
                Controller.getInstance().noteAdd(note);

                Controller.getInstance().noteEditHide();
            }
            editMode = false;
        }
    }
}
