package com.example.notes;

import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.HTMLEditor;


import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    //for accessing this controller from other controllers
    private static Controller instance;

    public Controller(){
        instance = this;
    }
    public static Controller getInstance(){
        return instance;
    }
    @FXML
    TilePane tilePane;
    @FXML
    BorderPane borderPane;
    @FXML
    VBox notesViewContainer;
    //Test button for switching containers
    @FXML
    Button testBtn;
    public void testBtnClick() throws IOException {
        System.out.println("123");
        notesViewContainer.setDisable(true);
        borderPane.setCenter(FXMLLoader.load(getClass().getResource("noteEdit.fxml")));
    }
    //Temp Id for testing
    private int tempId = 0;
    //TreeView for showing folders
    @FXML
    private TreeView<?> treeView;
    //List of notes stored before initialization
    private List<Note> notes;
    //Button for adding note
    @FXML
    private ImageView noteAddBtn;

    //happens when app first launched
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //notes stored before
        notes = new ArrayList<Note>(notes());
        try {
            for (int i = 0; i < notes.size(); i++) {
                //fxmlLoader for loading fxml file of a note
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("note.fxml"));
                VBox box = fxmlLoader.load();
                NoteController noteController = fxmlLoader.getController();
                noteController.setData(notes.get(i));
                tilePane.getChildren().add(0,box);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //temporary stored notes
    private List<Note> notes(){
        List<Note> ns = new ArrayList<Note>();
        for (int i =0;i<15;i++) {
            Note note = new Note();
            note.setId(tempId++);
            note.setText("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">Sample</span></p></body></html>");
            note.setTitle("Sample Title");
            ns.add(note);
        }
        return ns;
    }
    //Clicking on imageView of adding text note runs this method
    @FXML
    private void noteAdd() throws IOException {
        System.out.println("Success");
        Note note = new Note();
        note.setId(tempId++);
        note.setText("Sample Text");
        note.setTitle("Sample Title");
        notes.add(note);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("note.fxml"));
        VBox box = fxmlLoader.load();
        NoteController noteController = fxmlLoader.getController();
        noteController.setData(note);
        tilePane.getChildren().add(0,box);
    }
    public void noteEdit(int Id){
        System.out.println(Id);
        System.out.println(notes.get(Id).getText());
    }
    public void noteDelete(int Id){
        System.out.println(Id);
    }
    public void resizeTilePane(double width){
        tilePane.setPrefWidth(width-275);
    }
}
