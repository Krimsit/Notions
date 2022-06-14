package com.example.notes;

import java.util.concurrent.*;
import java.time.*;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Отвечает за создание отложенных уведомелний
 */
public class Notification {

    /**
     * Дата и время вызова уведомлений
     */
    private LocalDateTime notificationDate;

    /**
     * Текущая дата и время
     */
    private LocalDateTime now;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public Notification(LocalDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }


    /**
     * Создает фоновый поток, на котором запускается отложенный вызов уведомления
     */
    public void scheduleNotification() {
        now = LocalDateTime.now();

        notificationDate = notificationDate.plusSeconds(10);

        Duration duration = Duration.between(now, notificationDate);

        long initialDelay = duration.getSeconds();

        scheduler.schedule(new NotificationTask(scheduler), initialDelay, SECONDS);
        System.out.println("Thread started");
    }
}
