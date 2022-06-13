package com.example.notes;

import java.util.concurrent.*;
import java.time.*;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Notification {

    private LocalDateTime notificationDate;
    private LocalDateTime now;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public Notification(LocalDateTime notificationDate) {
        this.notificationDate = notificationDate;
    }

    public void scheduleNotification() {
        now = LocalDateTime.now();

        notificationDate = notificationDate.plusSeconds(10);

        Duration duration = Duration.between(now, notificationDate);

        long initialDelay = duration.getSeconds();

        scheduler.schedule(new NotificationTask(scheduler), initialDelay, SECONDS);
        System.out.println("Thread started");
    }
}
