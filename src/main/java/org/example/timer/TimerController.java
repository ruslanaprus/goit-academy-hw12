package org.example.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerController {

    private final ScheduledExecutorService scheduler;

    public TimerController() {
        scheduler = Executors.newScheduledThreadPool(4);
    }

    public void start() {
        FileController.clearFileContents();

        scheduler.scheduleAtFixedRate(() -> FileController.writeToFile(Timer.getTimeTask()), 0, 1000, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(() -> FileController.writeToFile(Timer.getMessageTask()), 5000, 5000, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(() -> CollectionController.addToDataList(Timer.getTimeTask()), 0, 1000, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(() -> CollectionController.addToDataList(Timer.getMessageTask()), 5000, 5000, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void shutdown() {
        System.out.println("Shutdown initiated...");
        CollectionController.readDataFromList();
        FileController.printFileContents();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Forcing shutdown...");
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
        System.out.println("Scheduler terminated.");
    }
}
