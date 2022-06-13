module com.example.notes {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires FXTrayIcon;
    requires com.google.api.client;
    requires google.api.client;
    requires com.google.api.services.calendar;
    requires com.google.api.client.json.gson;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;

    requires jdk.httpserver;
    requires com.jfoenix;

    opens com.example.notes to javafx.fxml;
    exports com.example.notes;
}