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
        System.out.println("Thread closed");

        try {
            // Wait a while for existing tasks to terminate
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                System.out.println("Thread closed");
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            scheduler.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
            System.out.println("Thread interrupted");
            Helper.writeException(ie);
        }
    }
}
