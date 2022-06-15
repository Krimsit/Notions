package com.example.notes;

import com.example.model.Note;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Основной класс, реализующий логику работы главного окна заметок.
 */
public class Controller implements Initializable {
    /**
     * Синглтон экземпляра Controller
     */
    private static Controller instance;

    /**
     * Базовый конструктор
     */
    public Controller() {
        instance = this;
    }

    /**
     * Получает ссылку на синглтон класса Controller
     *
     * @return Возвращает ссылку на синглтон класса Controller
     */
    public static Controller getInstance() {
        return instance;
    }

    @FXML
    public TilePane tilePane;
    @FXML
    public BorderPane borderPane;
    @FXML
    public VBox noteEditContainer;
    @FXML
    public VBox notesViewContainer;
    @FXML
    private TreeView<?> treeView;
    @FXML
    private Button noteAddTextNote;


    private Integer id = 0;

    private List<Note> notificationNotes = new ArrayList<Note>();


    /**
     * Вызывется при первом запуске приложения
     *
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
    public void updateViewedNotes() {
        clearEmptyFolder();
        createNotesTreeView();
        createNotesPlane();
    }

    /**
     * Создаеёт TreeView на основе папки с заметками
     */
    private void createNotesTreeView() {
        File baseFolder = new File("NotesStored");

        if (baseFolder.exists()) {
            File[] fileList = baseFolder.listFiles();

            TreeItem rootTreeItem = new TreeItem(baseFolder.getName());

            treeView.setShowRoot(false);

            try {
                for (File file : fileList) {
                    getNotesTreeBranches(file, rootTreeItem);
                }

                treeView.setRoot(rootTreeItem);

                treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<?>>() {
                    @Override
                    public void changed(ObservableValue<? extends TreeItem<?>> observable, TreeItem<?> oldValue, TreeItem<?> newValue) {
                        if (newValue != null) {
                            TreeItem<String> selectedItem = (TreeItem<String>) newValue;

                            NoteEditController.getInstance().edit(selectedItem.getValue());
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Helper.writeException(e);
            }
        }
    }

    /**
     * Создаеёт TreeView на основе папки с заметками
     */
    private void createNotesPlane() {
        tilePane.getChildren().clear();

        List<Note> notes = new ArrayList<Note>(getAllNotes());

        try {
            for (Note note : notes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("note.fxml"));
                VBox box = fxmlLoader.load();

                NoteController noteController = fxmlLoader.getController();
                noteController.setData(note);

                tilePane.getChildren().add(0, box);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Helper.writeException(e);
        }
    }

    /**
     * Получает спискок сохраненных заметок
     *
     * @return Возвращает список заметок
     */
    private List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<Note>();

        List<File> noteFiles = getNoteFilesInFolder("NotesStored");

        for (File noteFile : noteFiles) {
            if (noteFile.isFile()) {
                ObjectInputStream objectInputStream = null;

                try {
                    FileInputStream fileInputStream = new FileInputStream(noteFile);

                    if (fileInputStream != null) {
                        objectInputStream = new ObjectInputStream(fileInputStream);
                        Note note = (Note) objectInputStream.readObject();

                        notes.add(note);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Helper.writeException(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    Helper.writeException(e);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Helper.writeException(e);
                } finally {
                    if (objectInputStream != null) {
                        try {
                            objectInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Helper.writeException(e);
                        }
                    }
                }
            }
        }

        notes = notes.stream().sorted(Comparator.comparing(Note::getCreatedOn)).collect(Collectors.toList());

        //getAllNotesWithNotificationOn(notes);

        return notes;
    }

    /**
     * Получает все файлы заметок из всех папок
     *
     * @return Возвращает список файлов заметок
     */
    private List<File> getNoteFilesInFolder(String directoryName) {
        List<File> files = new ArrayList<>();

        File baseFolder = new File(directoryName);

        if (baseFolder.exists()) {
            for (File file : baseFolder.listFiles()) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    files.addAll(getNoteFilesInFolder(file.getAbsolutePath()));
                }
            }
        }

        return files;
    }

    /**
     * Получает и создаёт объекты TreeView
     *
     * @param baseEntry  начальный файл
     * @param parentNode родительская ветка
     * @return Возвращает список заметок
     */
    private void getNotesTreeBranches(File baseEntry, TreeItem parentNode) {
        if (baseEntry.isDirectory()) {
            TreeItem branch = new TreeItem(baseEntry.getName());

            branch.setExpanded(true);

            parentNode.getChildren().add(branch);

            for (File entry : baseEntry.listFiles()) {
                getNotesTreeBranches(entry, branch);
            }
        } else {
            TreeItem node = new TreeItem(baseEntry.getName().replace(".bin", ""));

            parentNode.getChildren().add(node);
        }
    }

    /**
     * Удаляет пустые папки
     */
    private void clearEmptyFolder() {
        File baseFolder = new File("NotesStored");

        if (baseFolder.exists()) {
            for (File fonderEntry : baseFolder.listFiles()) {
                if (fonderEntry.isDirectory()) {
                    fonderEntry.delete();
                }
            }
        }
    }

    /**
     * Находит объект заметки
     *
     * @param noteName имя файла
     * @return Объект заметки
     */
    public Note findNote(String noteName) {
        if (noteName != null) {
            List<Note> notes = new ArrayList<Note>(getAllNotes());

            for (Note note : notes) {
                if (note.getTitle().equals(noteName)) {
                    return note;
                }
            }
        }

        return new Note();
    }

    /**
     * Находит файл заметки
     *
     * @param noteName имя файла
     * @return файл заметки
     */
    public File findNoteFile(String noteName) {
        if (!noteName.isEmpty()) {
            List<File> noteFiles = new ArrayList<File>(getNoteFilesInFolder("NotesStored"));

            for (File file : noteFiles) {
                if (file.getName().equals(noteName + ".bin")) {
                    return file;
                }
            }
        }

        return null;
    }


    /**
     * Получает текущую дату
     *
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
     *
     * @param width ширина окна
     */
    public void resizeTilePane(double width) {
        tilePane.setPrefWidth(width - 275);
    }

    /**
     * Открывает окно добавления заметки
     */
    @FXML
    public void addNote() {
        NoteEditController.getInstance().edit(null);
    }

    public void getAllNotesWithNotificationOn(List<Note> notes) {
        notes.forEach((Note note) -> {
            if (note.getNotificationStatus()) {
                notificationNotes.add(note);
            }
        });
    }
}
