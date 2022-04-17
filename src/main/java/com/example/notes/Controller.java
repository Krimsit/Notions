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
    private static Controller instance;

    public Controller(){
        instance = this;
    }
    public static Controller getInstance(){
        return instance;
    }





    @FXML
    VBox editVBox =new VBox();
    @FXML
    BorderPane borderPane;
    @FXML
    HTMLEditor htmlEditor = new HTMLEditor();
    //VBox that contains all notes
    @FXML
    VBox notesViewContainer;
    //Main scrollPane
    @FXML
    ScrollPane mainScrollPane;
    //Test button for switching containers
    @FXML
    Button testBtn;
    public void testBtnClick(){
        System.out.println("123");
        notesViewContainer.setDisable(true);
        //borderPane.setCenter(htmlEditor);
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(actionEvent ->{

            String htmlText = htmlEditor.getHtmlText();
            System.out.println(htmlText);
        });
        borderPane.setCenter(editVBox);
        editVBox.getChildren().add(htmlEditor);
        editVBox.getChildren().add(saveBtn);

    }
    public void saveBntClick(){

    }


    //Temp Id for testing
    private int tempId = 0;


    //TreeView for showing folders
    @FXML
    private TreeView<?> treeView;
    //Grid Pane for (VBox)Notes
    @FXML
    private GridPane notesGrid;
    //List of notes stored before initialization
    private List<Note> notes;
    //Button for adding note (unused)
    @FXML
    private ImageView noteAddBtn;

    //starting columns
    private int columns = 0;
    //starting rows
    private int rows = 1;

    //happens when app first launched
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //notes stored before
        notes = new ArrayList<Note>(notes());
        try {
            // adding notes to main gridpane
            for (int i = 0; i<notes.size();i++){
                //fxmlLoader for loading fxml file of a note
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("note.fxml"));
                VBox box = fxmlLoader.load();
                NoteController noteController = fxmlLoader.getController();
                noteController.setData(notes.get(i));
                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }

                //adding (VBox)note to gridPane
                notesGrid.add(box,columns++,rows);
                GridPane.setMargin(box,new Insets(10));
                //Убирается 0 ряд (Работает но надо лучше переделать)
                while(notesGrid.getRowConstraints().size() > 0){
                    notesGrid.getRowConstraints().remove(0);
                }

                while(notesGrid.getColumnConstraints().size() > 0) {
                    notesGrid.getColumnConstraints().remove(0);
                }
            }
        }
        catch (IOException e) {
                e.printStackTrace();
        }
    }
    //temporary stored notes
    private List<Note> notes(){
        List<Note> ns = new ArrayList<Note>();
        for (int i =0;i<15;i++) {
            Note note = new Note();
            note.setId(tempId++);
            note.setText("Sample Text");
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
        if (columns == 3){
            columns = 0;
            ++rows;
        }
        notesGrid.add(box,columns++,rows);
        GridPane.setMargin(box,new Insets(10));
    }
    public void noteEdit(int Id){
        System.out.println(Id);
    }
    public void noteDelete(int Id){
        System.out.println(Id);
    }
}
