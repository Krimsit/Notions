package com.example.notes;
import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NoteEditController {

    @FXML
    private Button noteEditCancelBtn;

    @FXML
    private TextField noteEditTitle;

    @FXML
    private Button noteEditSaveBtn;

    @FXML
    private HTMLEditor noteEditText;


    @FXML
    private void noteEditCancelBtnClicked(){
        Controller.getInstance().noteEditHide();
    }
    @FXML
    private void noteEditSaveBtnClicked(){
        Note note = new Note();
        note.setId(Controller.getInstance().getLastId()+1);
        note.setText(noteEditText.getHtmlText());
        note.setTitle(noteEditTitle.getText());
        LocalDateTime now = LocalDateTime.now();
        note.setCreatedOn(now);
        if (Controller.getInstance().checkNonUniqueName(note.getTitle())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.show();
        }
        else {
            Controller.getInstance().noteAdd(note);
            Controller.getInstance().noteEditHide();
        }
    }
}
