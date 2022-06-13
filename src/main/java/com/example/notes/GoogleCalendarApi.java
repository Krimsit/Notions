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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* class to demonstarte use of Calendar events list API */
public class GoogleCalendarApi {
    /** Application name. */
    private static final String APPLICATION_NAME = "Notions App";
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /** Directory to store authorization tokens for this application. */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);


    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
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

    public void createEvent() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();



        Event event = new Event()
                .setSummary("Я создался из проекта по ПнП 😎");


        DateTime startDateTime = new DateTime(String.valueOf(LocalDateTime.now()));
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime);
        event.setStart(start);

        event.setDescription("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p style=\"text-align: left;\"><span style=\"font-family: &quot;&quot;;\">Привет. Я </span><span style=\"font-family: Algerian;\">создан из </span><span style=\"font-family: &quot;Bauhaus 93&quot;;\">проекта <span style=\"font-size: -webkit-xxx-large;\">ПнП&nbsp;</span></span></p><p style=\"text-align: left;\"><span class=\"Apple-tab-span\" style=\"white-space: pre;\"><span style=\"font-size: large;\"><span style=\"font-family: &quot;Bauhaus 93&quot;;\">\tИ поддерживаю </span><span style=\"font-family: &quot;Courier New&quot;;\">HTML формат.</span></span></span></p><p style=\"text-align: left;\"><span class=\"Apple-tab-span\" style=\"white-space: pre;\"><span style=\"font-family: &quot;Bauhaus 93&quot;; font-size: -webkit-xxx-large;\">Смотри </span><span style=\"font-family: &quot;Bauhaus 93&quot;; font-size: x-large; font-style: italic; font-weight: bold; text-decoration: underline line-through;\">какой я крутой&nbsp;</span></span></p><p style=\"text-align: left;\"><span class=\"Apple-tab-span\" style=\"white-space: pre;\"><span style=\"font-family: &quot;Bauhaus 93&quot;; font-size: x-large; font-style: italic; font-weight: bold; text-decoration: underline line-through;\"><br></span></span></p><p style=\"text-align: left;\"></p><ol><li><span style=\"font-family: &quot;Courier New&quot;; font-size: x-large; white-space: pre;\">Кр</span></li><li><span style=\"font-family: &quot;Courier New&quot;; font-size: x-large; white-space: pre;\">Ут</span></li><li><span style=\"font-family: &quot;Courier New&quot;; font-size: x-large; white-space: pre;\">Ой</span></li></ol><div style=\"text-align: center;\"></div><p></p><p></p><p></p><hr style=\"text-align: center;\"><p></p><p></p></body></html>");

        DateTime endDateTime = new DateTime(String.valueOf(LocalDateTime.now()));
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime);
        event.setEnd(end);



        String calendarId = "primary";
        event = service.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());


    }
}