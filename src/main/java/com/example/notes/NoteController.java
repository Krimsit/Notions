package com.example.notes;

import com.example.notes.Controller;
import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import static java.lang.Integer.parseInt;

public class NoteController {
    @FXML
    private Label noteId;

    @FXML
    private ImageView noteDelete;

    @FXML
    private ImageView noteEditBtn;

    @FXML
    private Label noteText;

    @FXML
    private Label noteTitle;

    public void setData(Note note){
        noteId.setText(note.getId().toString());
        noteTitle.setText(note.getTitle());
        noteText.setText(note.getText());

    }
    @FXML
    public void editNoteBtnClicked(MouseEvent mouseEvent) {
        int Id = parseInt(noteId.getText());
        Controller.getInstance().noteEdit(Id);
    }
    @FXML
    public void deleteNoteBtnClicked(MouseEvent mouseEvent) {
        int Id = parseInt(noteId.getText());
        Controller.getInstance().noteDelete(Id);
    }
}
