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
import java.nio.file.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Controller implements Initializable {
    //for accessing this controller from other controllers
    private static Controller instance;
    public Controller(){
        instance = this;
    }
    public static Controller getInstance(){
        return instance;
    }
    VBox noteEditBox;
    {
        try {
            noteEditBox = FXMLLoader.load(getClass().getResource("noteEdit.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    TilePane tilePane;
    @FXML
    BorderPane borderPane;
    @FXML
    VBox notesViewContainer;
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

    private Integer id = 0;
    //happens when app first launched
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateViewedNotes();
    }
    private void updateViewedNotes(){
        notes = new ArrayList<Note>(getStoredNotes());
        //notes stored before
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
    //returns stored notes sorted by date
    private List<Note> getStoredNotes(){
        List<Note> ns = new ArrayList<Note>();
        File folder = new File("NotesStored");
        if (!folder.exists()) folder.mkdirs();
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                //System.out.println(file.getName());
                //System.out.println(folder + file.getName());
                File noteFile = new File(folder + "/" + file.getName());
                ObjectInputStream objectInputStream = null;
                try {
                    FileInputStream fileInputStream = new FileInputStream(noteFile);
                    if (fileInputStream != null) {
                        objectInputStream = new ObjectInputStream(fileInputStream);
                        Note note = (Note) objectInputStream.readObject();
                        ns.add(note);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                finally {
                    if (objectInputStream!=null){
                        try {
                            objectInputStream.close();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        ns = ns.stream().sorted(Comparator.comparing(Note::getCreatedOn)).collect(Collectors.toList());
        return ns;
    }
    //temporary stored notes
    /*
    private List<Note> notes(){
        List<Note> ns = new ArrayList<Note>();
        for (int i =0;i<15;i++) {
            Note note = new Note();
            note.setId(tempId++);
            note.setText("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">Sample"+i+"</span></p></body></html>");
            note.setTitle("Sample Title"+i);
            try {

                FileOutputStream fileOutputStream = new FileOutputStream(note.getTitle()+".bin");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(note);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("File not found");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("I");
            }
            ns.add(note);
        }
        return ns;
    }

     */
    //Clicking on imageView of adding text note runs this method
    @FXML
    private void noteAddBtnClicked() throws IOException {
        System.out.println("Success");
        try {
            noteEditBox = FXMLLoader.load(getClass().getResource("noteEdit.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        notesViewContainer.setDisable(true);
        borderPane.setCenter(noteEditBox);

        /*
        note.setId(tempId++);
        note.setText("Sample Text");
        note.setTitle("Sample Title");
         */

    }
    public int getLastId(){
        System.out.println("Here");
        if (notes.size()==0){
            return 0;
        }
        else {
            return notes.get(notes.size() - 1).getId();
        }
    }
    public boolean checkNonUniqueName(String name){
        File folder = new File("NotesStored");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (file.getName().equals(name + ".bin")){
                    return true;
                }
            }
        }
        return false;
    }
    public void noteAdd(Note note){
        File directory = new File("NotesStored");
        if (!directory.exists()) directory.mkdirs();
        File file = new File("NotesStored/"+note.getTitle()/*+note.getId()*/+".bin");
        ObjectOutputStream objectOutputStream = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (fileOutputStream !=null){
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(note);
                notes.add(note);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("note.fxml"));
                VBox box = fxmlLoader.load();
                NoteController noteController = fxmlLoader.getController();
                noteController.setData(notes.get(notes.size()-1));
                tilePane.getChildren().add(0,box);
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            if (objectOutputStream!=null){
                try {
                    objectOutputStream.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void noteEditHide(){
        notesViewContainer.setDisable(false);
        noteEditBox.setDisable(true);
        borderPane.setCenter(notesViewContainer);
    }
    public void noteEdit(String title){
        System.out.println(title);
        System.out.println("Нашлось");
        File folder = new File("NotesStored");
        File file = new File(folder+"/"+title+".bin");
        if (file.isFile()) {
            //System.out.println(file.getName());
            //System.out.println(folder + file.getName());
            Note note = new Note();
            ObjectInputStream objectInputStream = null;
            System.out.println("Its a file");
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                if (fileInputStream != null) {
                    objectInputStream = new ObjectInputStream(fileInputStream);
                    note = (Note) objectInputStream.readObject();
//                    System.out.println(note.getId());
//                    System.out.println(note.getTitle());
//                    System.out.println(note.getCreatedOn());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                if (objectInputStream!=null){
                    try {
                        objectInputStream.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            try {
                noteEditBox = FXMLLoader.load(getClass().getResource("noteEdit.fxml"));
                NoteEditController.getInstance().setEditMode(note);
            } catch (IOException e) {
                e.printStackTrace();
            }
            notesViewContainer.setDisable(true);
            borderPane.setCenter(noteEditBox);
        }
    }
    public void noteDelete(String title) throws IOException {
        try {
            Files.deleteIfExists(
                    Paths.get("NotesStored\\"+title+".bin"));
        }
        catch (NoSuchFileException e) {
            System.out.println(
                    "No such file/directory exists");
        }
        catch (DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty.");
        }
        catch (IOException e) {
            System.out.println("Invalid permissions.");
        }
        tilePane.getChildren().clear();
        updateViewedNotes();

    }
    public void resizeTilePane(double width){
        tilePane.setPrefWidth(width-275);
    }
}
