package com.example.notes;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.notes.NoteApplication.trayIcon;

/**
 * Класс который вызывается на фоновом потоке и непосредственно вызывает отложенное уведомление
 */
public class NotificationTask implements Runnable {

    private ScheduledExecutorService scheduler;

    public NotificationTask(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        if (trayIcon.isSupported()) {
            trayIcon.showMessage("Проверка");
            Helper.playSound(Helper.SoundType.NOTIFICATION);

            terminateScheduledThread();
        }
    }


    public void terminateScheduledThread() {
        scheduler.shutdown(); // Disable new tasks from being submitted

        try {
            // Wait a while for existing tasks to terminate
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {}
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            scheduler.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
            Helper.writeException(ie);
        }
    }
}
