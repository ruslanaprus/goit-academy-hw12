package org.example.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerController {

    private final ScheduledExecutorService scheduler;
    private final DataController dataController;

    public TimerController(DataController dataController) {
        scheduler = Executors.newScheduledThreadPool(2);
        this.dataController = dataController;
    }

    public void start() {
        FileController.clearData();

        scheduler.scheduleAtFixedRate(() -> dataController.writeData(Timer.getTimeTask()), 0, 1000, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(() -> dataController.writeData(Timer.getMessageTask()), 5000, 5000, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void shutdown() {
        System.out.println("Shutdown initiated...");
        dataController.readData();
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
