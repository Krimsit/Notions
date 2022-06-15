package com.example.notes;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;


import java.io.*;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.notes.NoteApplication.trayIcon;

/* class to demonstarte use of Calendar events list API */
public class GoogleCalendarApi {
    /**
     * Синглтон класса GoogleCalendarApi
     */
    private static GoogleCalendarApi instance;

    /**
     * Базовый конструктор
     */
    public GoogleCalendarApi() {
        instance = this;
    }

    /**
     * Получает ссылку на синглтон класса GoogleCalendarApi
     *
     * @return Возвращает ссылку на синглтон класса GoogleCalendarApi
     */
    public static GoogleCalendarApi getInstance() {
        return instance;
    }

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Notions App";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);


    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleCalendarApi.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    /**
     * Производит авторизацию пользователя к Google Calendar API и используя протокол HTTP отправляет запрос на создание мероприятия в Google Calendar
     *
     * @param title              заголовок события
     * @param description        описание события. Может содержть HTML текст
     * @param startTime          начало события в формате OffsetDateTime
     * @param endTime            окончание события в формате OffsetDateTime
     * @param useDefaultReminder true если использовать напоминание по умолчанию, false если нужно отключить напоминание
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void addEventToGoogleCalendar(String title, String description, OffsetDateTime startTime, OffsetDateTime endTime, Boolean useDefaultReminder) throws IOException, GeneralSecurityException {
        try {
            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();


            Event event = new Event().setSummary(title);

            event.setDescription(description);

            DateTime startDateTime = new DateTime(String.valueOf(startTime));
            EventDateTime start = new EventDateTime().setDateTime(startDateTime);
            event.setStart(start);

            DateTime endDateTime = new DateTime(String.valueOf(endTime));
            EventDateTime end = new EventDateTime().setDateTime(endDateTime);
            event.setEnd(end);

            if (!useDefaultReminder) {
                Event.Reminders reminders = new Event.Reminders();
                reminders.setUseDefault(false);
                event.setReminders(reminders);
            }

            String calendarId = "primary";

            event = service.events().insert(calendarId, event).execute();
            System.out.printf("Event created: %s\n", event.getHtmlLink());

            trayIcon.showMessage("Событие успешно добавлено в ваш календарь");
            Helper.playSound(Helper.SoundType.EVENT);
        } catch (RuntimeException ex) {
            trayIcon.showErrorMessage("Ошибка!", "Событие не добавлено в ваш календарь. Проверьте логи");
            Helper.writeException(ex);
        }
    }

    /**
     * Производит авторизацию пользователя к Google Calendar API и используя протокол HTTP отправляет запрос на создание мероприятия в Google Calendar
     *
     * @param title        заголовок события
     * @param description  описание события. Может содержть HTML текст
     * @param startTime    начало события в формате OffsetDateTime
     * @param endTime      окончание события в формате OffsetDateTime
     * @param reminderType формат напоминания. 0 - уведомление, 1 - письмо на почту
     * @param reminderTime количество минут до начала события, когда должно сработать напоминание.
     *                     Допустимые значения от 0 до 40320 (4 недели)
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public void addEventToGoogleCalendar(String title, String description, OffsetDateTime startTime, OffsetDateTime endTime,
                                         int reminderType, int reminderTime) throws IOException, GeneralSecurityException {
        try {
            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            Event event = new Event().setSummary(title);

            event.setDescription(description);

            DateTime startDateTime = new DateTime(String.valueOf(startTime));
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("Europe/Moscow");
            event.setStart(start);

            DateTime endDateTime = new DateTime(String.valueOf(endTime));
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("Europe/Moscow");
            event.setEnd(end);

            Event.Reminders temp = event.getReminders();

            Event.Reminders reminders = new Event.Reminders();
            EventReminder eventReminder = new EventReminder();
            List<EventReminder> eventReminderList = new ArrayList<EventReminder>();

            reminders.setUseDefault(false);

            switch (reminderType) {
                case 0:
                    eventReminder.setMethod("popup");
                    eventReminder.setMinutes(reminderTime);
                    break;
                case 1:
                    eventReminder.setMethod("email");
                    eventReminder.setMinutes(reminderTime);
                    break;
            }

            eventReminderList.add(eventReminder);
            reminders.setOverrides(eventReminderList);
            event.setReminders(reminders);

            String calendarId = "primary";

            event = service.events().insert(calendarId, event).execute();
            System.out.printf("Event created: %s\n", event.getHtmlLink());

            trayIcon.showMessage("Событие успешно добавлено в ваш календарь", "");
            Helper.playSound(Helper.SoundType.EVENT);
        } catch (RuntimeException ex) {
            trayIcon.showErrorMessage("Ошибка!", "Событие не добавлено в ваш календарь. Проверьте логи");
        }
    }
}