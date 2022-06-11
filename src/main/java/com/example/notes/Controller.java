package com.example.notes;

import com.example.model.Note;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.nio.file.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Основной класс, реализующий логику работы главного окна заметок.
 */
public class Controller implements Initializable {
    //for accessing this controller from other controllers
    /**
     * Синглтон экземпляра Controller
     */
    private static Controller instance;

    /**
     * Базовый конструктор
     */
    public Controller(){
        instance = this;
    }

    /**
     * Получить ссылку на синглтон класса Controller
     * @return Возвращает ссылку на синглтон класса Controller
     */
    public static Controller getInstance(){
        return instance;
    }

    /**
     * Поле окна редактирования заметки
     */
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
    private Button noteAddTextNote;

    private Integer id = 0;

    /**
     * Вызывется при первом запуске приложения
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateViewedNotes();

        Animation.ScaleButtonAnimation(noteAddTextNote);
        Animation.CreateTooltip(noteAddTextNote, "Создать текстовую заметку");
    }

    /**
     * Метод обновляет UI отображение заметок на главном окне
     */
    private void updateViewedNotes(){
        notes = new ArrayList<Note>(getStoredNotes(new File("NotesStored")));

        try {
            for (Note note : notes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("note.fxml"));
                VBox box = fxmlLoader.load();

                NoteController noteController = fxmlLoader.getController();
                noteController.setData(note);

                tilePane.getChildren().add(0, box);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод получения списка сохраненных заметок
     * @param baseFolder папка, где хранятся заметки
     * @return Возвращает список заметок
     */
    private List<Note> getStoredNotes(File baseFolder){
        List<Note> noteFiles = new ArrayList<Note>();

        getNotesInFolder(baseFolder, noteFiles);

        noteFiles = noteFiles.stream().sorted(Comparator.comparing(Note::getCreatedOn)).collect(Collectors.toList());

        return noteFiles;
    }

    /**
     * Метод получения списка сохраненных заметок
     * @param baseFolder папка, где хранятся заметки
     * @return Возвращает список заметок
     */
    private List<Note> getNotesInFolder(File baseFolder, List<Note> noteFiles){
        File[] folderEntries = baseFolder.listFiles();

        for (File folderEntry : folderEntries) {
            if (folderEntry.isDirectory())
            {
                getNotesInFolder(folderEntry, noteFiles);
                continue;
            }

            if (folderEntry.isFile()) {
                File noteFile = new File(folderEntry.getPath());

                ObjectInputStream objectInputStream = null;

                try {
                    FileInputStream fileInputStream = new FileInputStream(noteFile);

                    if (fileInputStream != null) {
                        objectInputStream = new ObjectInputStream(fileInputStream);
                        Note note = (Note) objectInputStream.readObject();

                        noteFiles.add(note);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                finally {
                    if (objectInputStream != null){
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

        return noteFiles;
    }

    /**
     * Открывает окно добавления заметки
     */
    @FXML
    private void noteAddTextNoteClicked() throws IOException {
        try {
            noteEditBox = FXMLLoader.load(getClass().getResource("noteEdit.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        notesViewContainer.setDisable(true);
        borderPane.setCenter(noteEditBox);
    }

    /**
     * Возвращает идентификатор последней записки в списке
     */
    public int getLastId(){
        if (notes.size()==0){
            return 0;
        }
        else {
            return notes.get(notes.size() - 1).getId();
        }
    }

    /**
     * Проверяет название создаваемого файла на уникальность
     * @param name название создаваемого файла
     * @return true если имя неуникально
     */
    public boolean checkNonUniqueName(String name){
        File folder = new File("NotesStored/");
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

    /**
     * Метод сохраняет заметку на компьютер и запускает сразу режим редактирования
     * @param baseFolder директория, где хранятся заметки
     * @param note заметка Note
     * @see Note
     */
    public void noteAdd(String baseFolder, Note note){
        File directory = new File(baseFolder + "/" + getCurrentDate());
        if (!directory.exists()) directory.mkdirs();

        File file = new File(baseFolder + "/" + getCurrentDate() + "/" + note.getTitle().replaceAll(" ", "_") + ".bin");

        ObjectOutputStream objectOutputStream = null;

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            if (fileOutputStream != null){
                objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(note);

                updateViewedNotes();
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

    /**
     * Скрывает режим редактирования заметки
     */
    public void noteEditHide(){
        notesViewContainer.setDisable(false);
        noteEditBox.setDisable(true);
        borderPane.setCenter(notesViewContainer);
    }

    /**
     * Производит поиск заметки на компьютере и в случае успеха переходит в режим ее редактирования
     * @param baseFolder директория, где хранятся заметки
     * @param title заголовок заметки по которому происходит поиск
     */
    public void noteEdit(String baseFolder, String title, String createdOn){
        File file = new File(baseFolder+"/"+createdOn+"/"+title+".bin");

        if (file.isFile()) {
            Note note = new Note();

            ObjectInputStream objectInputStream = null;

            try {
                FileInputStream fileInputStream = new FileInputStream(file);

                if (fileInputStream != null) {
                    objectInputStream = new ObjectInputStream(fileInputStream);
                    note = (Note) objectInputStream.readObject();

                    updateViewedNotes();
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

    /**
     * Производит поиск заметки на компьютере и в случае успеха удаляет ее
     * @param title заголовок заметки
     * @throws IOException
     * @throws NoSuchFileException
     * @throws DirectoryNotEmptyException
     */
    public void noteDelete(String title, String createdTime) throws IOException {
        try {
            Files.deleteIfExists(
                    Paths.get("NotesStored\\"+createdTime+"\\"+title+".bin"));
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


    /**
     * Получает текущую дату
     * @return текущая дата в формате dd-MM-yy
     */
    public String getCurrentDate() {
        Date date = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        String _date = formatter.format(date);

        return _date;
    }

    /**
     * Меняет размер окна с заметками
     * @param width ширина окна
     */
    public void resizeTilePane(double width){
        tilePane.setPrefWidth(width-275);
    }
}
