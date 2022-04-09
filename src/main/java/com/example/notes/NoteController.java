package com.example.notes;

import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class NoteController {

    @FXML
    private ImageView noteDelete;

    @FXML
    private ImageView noteEdit;

    @FXML
    private Label noteText;

    @FXML
    private Label noteTitle;

    public void setData(Note note){
        noteTitle.setText(note.getTitle());
        noteText.setText(note.getText());

    }
}
