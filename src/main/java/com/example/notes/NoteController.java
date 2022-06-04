package com.example.notes;

import com.example.notes.Controller;
import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.Integer.parseInt;

public class NoteController {
    private static NoteController instance;

    public NoteController(){
        instance = this;
    }
    public static NoteController getInstance(){
        return instance;
    }

    @FXML
    private VBox noteContainer;
    @FXML
    private Label noteId;

    @FXML
    private ImageView noteDelete;

    @FXML
    private ImageView noteEditBtn;

    @FXML
    private WebView noteText;
    private WebEngine noteEngine;

    @FXML
    private Label noteTitle;

    @FXML
    private Label noteDate;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
    public void setData(Note note){
        noteId.setText(note.getId().toString());
        noteTitle.setText(note.getTitle());
        noteEngine = noteText.getEngine();
        noteEngine.loadContent(note.getText());
        noteDate.setText(dtf.format(note.getCreatedOn()));
    }
    @FXML
    public void editNoteBtnClicked(MouseEvent mouseEvent) {
        String title = noteTitle.getText();
        Controller.getInstance().noteEdit(title);
    }
    @FXML
    public void deleteNoteBtnClicked(MouseEvent mouseEvent) throws IOException {
        String title = noteTitle.getText();
        Controller.getInstance().noteDelete(title);
    }

    public void resizeNote(double windowWidth){
    noteContainer.setPrefWidth((windowWidth-244)/3);
    }
}
