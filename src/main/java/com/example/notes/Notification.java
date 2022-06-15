package com.example.notes;

import com.example.model.Note;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.time.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Notification implements Initializable {
    /**
     * Синглтон класса Notification
     */
    private static Notification instance;

    /**
     * Базовый конструктор
     */
    public Notification() {
        instance = this;
    }


    /**
     * Получает ссылку на синглтон класса NoteEditController
     *
     * @return Возвращает ссылку на синглтон класса Notification
     */
    public static Notification getInstance() {
        return instance;
    }

    @FXML
    private MFXDatePicker notificationDatePicker;
    @FXML
    private MFXTextField notificationTimeHour;
    @FXML
    private MFXTextField notificationTimeMinute;

    private MFXGenericDialog dialogContent;
    private MFXStageDialog dialog;

    private Note note;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notificationDatePicker.setValue(LocalDate.now());
        notificationTimeHour.setText("0");
        notificationTimeMinute.setText("0");
    }

    /**
     * Открывает модальное окно настройки уведомлений заметки
     *
     * @param noteObj Объект создаваемой заметки
     */
    public void openModal(Note noteObj) {
        note = noteObj;

        VBox text = new VBox();

        try {
            text = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("notificationSettings.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        VBox finalText = text;

        dialogContent = MFXGenericDialogBuilder.build()
                .setHeaderText("Настройка уведомлений")
                .setContent(finalText)
                .get();

        dialog = MFXGenericDialogBuilder.build(dialogContent)
                .toStageDialogBuilder()
                .setDraggable(true)
                .setTitle("Loading...")
                .get();

        dialogContent.addActions(
                Map.entry(new MFXButton("Сохранить"), event -> {
                    try {
                        MFXDatePicker notificationDatePicker = (MFXDatePicker) dialogContent.lookup("#notificationDatePicker");
                        MFXToggleButton notificationInSystem = (MFXToggleButton) dialogContent.lookup("#notificationInSystem");
                        MFXToggleButton notificationInGoogleCalendar = (MFXToggleButton) dialogContent.lookup("#notificationInGoogleCalendar");
                        MFXTextField notificationTimeHour = (MFXTextField) dialogContent.lookup("#notificationTimeHour");

                        LocalDate date = notificationDatePicker.getValue();
                        Boolean isSystem = notificationInSystem.isSelected();
                        Boolean isGoogleCalendar = notificationInGoogleCalendar.isSelected();
                        Integer hour = Integer.parseInt(notificationTimeHour.getText());
                        Integer minute = Integer.parseInt(notificationTimeHour.getText());

                        saveSettings(date, hour, minute, isSystem, isGoogleCalendar);

                        dialog.close();
                        NoteEditController.getInstance().closeEdit();
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }),
                Map.entry(new MFXButton("Назад"), event -> {
                    note = null;
                    dialog.close();
                })
        );

        dialogContent.setMaxSize(300, 400);

        dialog.showDialog();
    }

    /**
     * Сохраняет настройки уведомлений и сохраняет заметку
     *
     * @param date Дата заметки
     * @param hour Час уведомления
     * @param minute Минута уведомления
     * @param isSystem Будет ли уведомление показываться в системе
     * @param isGoogleCalendar Будет ли уведомление добавлено в Google Calendar
     */
    public void saveSettings(LocalDate date, Integer hour, Integer minute, Boolean isSystem, Boolean isGoogleCalendar) throws GeneralSecurityException, IOException {
        LocalDateTime notificationDate = date.atStartOfDay().withHour(hour).withMinute(minute).withSecond(1).withNano(1);

        if (isSystem || isGoogleCalendar) {
            note.setNotificationDate(notificationDate);
        }

        NoteEditController.getInstance().add(note);

        if (isSystem) {
            scheduleNotification(notificationDate);
        }

        if (isGoogleCalendar) {
            ZoneId zoneId = ZoneOffset.systemDefault();
            ZoneOffset zoneOffset = zoneId.getRules().getOffset(notificationDate);

            OffsetDateTime source = notificationDate.atOffset(zoneOffset);

            GoogleCalendarApi googleCalendarApi = new GoogleCalendarApi();

            googleCalendarApi.addEventToGoogleCalendar(note.getTitle(), note.getText(), source,
                    source, false);
        }
    }

    public void scheduleNotification(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(now, date);

        long initialDelay = duration.getSeconds();

        scheduler.schedule(new NotificationTask(scheduler), initialDelay, SECONDS);
    }
}
