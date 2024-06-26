package org.example.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The {@code SchedulerController} class manages the scheduling of tasks to write data at regular intervals.
 */
public class SchedulerController {

    private final ScheduledExecutorService scheduler;
    private final DataHandler dataHandler;

    /**
     * Constructs a new {@code SchedulerController} with the specified {@code DataHandler}.
     *
     * @param dataHandler the data handler
     */
    public SchedulerController(DataHandler dataHandler) {
        this(dataHandler, Executors.newScheduledThreadPool(2));
    }

    /**
     * Constructs a new {@code SchedulerController} with the specified {@code DataHandler} and {@code ScheduledExecutorService}.
     *
     * @param dataHandler the data handler
     * @param scheduler   the scheduler
     */
    public SchedulerController(DataHandler dataHandler, ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        this.dataHandler = dataHandler;
    }

    /**
     * Starts the scheduler to write data at regular intervals.
     */
    public void start() {
        if (dataHandler instanceof FileDataHandler) {
            ((FileDataHandler) dataHandler).clearData();
        }

        scheduler.scheduleAtFixedRate(() -> dataHandler.writeData(Timer.getTimeTask()), 0, 1000, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(() -> dataHandler.writeData(Timer.getMessageTask()), 5000, 5000, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void shutdown() {
        System.out.println("Shutdown initiated...");
        dataHandler.readData();
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
