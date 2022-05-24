package com.example.notes;
import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

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
    private void noteEditSaveBtnClicked(){
        Note note = new Note();
        note.setId(123);
        note.setText(noteEditText.getHtmlText());
        note.setTitle(noteEditTitle.getText());
        Controller.getInstance().noteAdd(note);
        Controller.getInstance().noteEditHide();
    }
}
