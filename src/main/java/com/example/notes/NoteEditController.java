package com.example.notes;

import com.example.model.Note;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;


/**
 * Контроллер редактирования заметки, реализующий логику наполнения и изменения заметки
 */
public class NoteEditController implements Initializable {
    /**
     * Синглтон класса NoteEditController
     */
    private static NoteEditController instance;

    /**
     * Базовый конструктор
     */
    public NoteEditController() {
        instance = this;
    }


    /**
     * Получает ссылку на синглтон класса NoteEditController
     *
     * @return Возвращает ссылку на синглтон класса NoteEditController
     */
    public static NoteEditController getInstance() {
        return instance;
    }

    @FXML
    private VBox noteEditContainer;
    @FXML
    private TextField noteEditTitle;
    @FXML
    private HTMLEditor noteEditText;
    @FXML
    private Button noteEditDeleteBtn;
    @FXML
    private Button noteEditNextBtn;
    @FXML
    private Button noteEditExitBtn;
    @FXML
    public VBox notificationSettingsContainer;
    {
        try {
            notificationSettingsContainer = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("notificationSettings.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean editMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Animation.ScaleButtonAnimation(noteEditNextBtn);
        Animation.ScaleButtonAnimation(noteEditExitBtn);
    }

    /**
     * Скрывает режим редактирования заметки
     */
    public void closeEdit() {
        Controller.getInstance().borderPane.setCenter(Controller.getInstance().notesViewContainer);
        Controller.getInstance().updateViewedNotes();

        editMode = false;
    }

    /**
     * Метод сохраняет/обновляет заметку на компьютер
     *
     * @param note заметка Note
     * @see Note
     */
    public void add(Note note) {

            File directory = new File("NotesStored/" + Controller.getInstance().getCurrentDate());

            if (!directory.exists()) directory.mkdirs();

            File file = new File(directory.getPath() + "/" + note.getTitle().replaceAll(" ", "_") + ".bin");

            ObjectOutputStream objectOutputStream = null;

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                if (fileOutputStream != null) {
                    objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(note);
                    fileOutputStream.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Helper.writeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                Helper.writeException(e);
            } finally {
                if (objectOutputStream != null) {
                    try {
                        objectOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Helper.writeException(e);
                    }
                }
            }
    }

    /**
     * Производит поиск заметки на компьютере и в случае успеха переходит в режим ее редактирования
     *
     * @param noteFileName заголовок заметки по которому происходит поиск
     */
    public void edit(String noteFileName) {
        Note note = new Note();
        if (noteFileName != null) {
            note = Controller.getInstance().findNote(noteFileName);
            editMode = true;
            noteEditTitle.setText(note.getTitle());
            noteEditText.setHtmlText(note.getText());
        }
        else {
            noteEditTitle.setText("");
            noteEditText.setHtmlText("");
        }
        Controller.getInstance().borderPane.setCenter(noteEditContainer);
    }

    /**
     * Производит поиск заметки на компьютере и в случае успеха удаляет ее
     *
     * @param noteName заголовок заметки
     */
    public void delete(String noteName) {
        File noteFile = Controller.getInstance().findNoteFile(noteName);

        String noteFilePath = noteFile.getAbsolutePath();

        try {
            Files.delete(Path.of(noteFilePath));
        } catch (NoSuchFileException e) {
            System.out.println(
                    "No such file/directory exists");
            Helper.writeException(e);
        } catch (DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty.");
            Helper.writeException(e);
        } catch (IOException e) {
            System.out.println("Invalid permissions.");
            Helper.writeException(e);
        }

        Controller.getInstance().updateViewedNotes();
    }

    /**
     * Проверяет название создаваемого файла на уникальность
     *
     * @param noteName название создаваемого файла
     * @return true если имя неуникально
     */
    private boolean checkNonUniqueName(String noteName) {
        Note note = Controller.getInstance().findNote(noteName);
        System.out.println(note.getTitle());
        System.out.println(noteName);

        if (note.getId() != null && note.getTitle().equals(noteName)) {
            return true;
        }

        return false;
    }

    /**
     * Вызывается при нажатии на кнопку отмены редактирования
     */
    @FXML
    private void exitFromEditMote() {
        closeEdit();
    }

    /**
     * Вызывается при нажатии на кнопку сохранить изменения
     */
    @FXML
    private void saveNote() {
        if (noteEditTitle.getText()==null){
            System.out.println("error");
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Ошибка!");
            alert.setContentText("Введите имя заметки");
            alert.setHeaderText("Внимание!");

            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image((Helper.class.getResource("/com/example/img/icon.png")).toString()));

            alert.show();
        }
        else {
            Note note = new Note();

            LocalDateTime createdOnDate = LocalDateTime.now();

            note.setText(noteEditText.getHtmlText());
            note.setTitle(noteEditTitle.getText());
            note.setCreatedOn(createdOnDate);

            if (!editMode) {
                String noteId = UUID.randomUUID().toString();
                note.setId(noteId);
            }

            if (checkNonUniqueName(note.getTitle())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Ошибка!");
                alert.setContentText("Заметка с таким имененм уже существует");
                alert.setHeaderText("Внимание!");

                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image((Helper.class.getResource("/com/example/img/icon.png")).toString()));

                alert.show();
            } else {
                try {
                    FXMLLoader.load(Objects.requireNonNull(getClass().getResource("notificationSettings.fxml")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Notification.getInstance().openModal(note);
            }
        }
    }

    /**
     * Вызывается при нажатии на кнопку удаления заметки
     */
    @FXML
    private void deleteNote() {
        String noteTitle = noteEditTitle.getText();

        delete(noteTitle);
    }
}
