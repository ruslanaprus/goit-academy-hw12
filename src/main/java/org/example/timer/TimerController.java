package org.example.timer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerController {

    private static final String FILE_PATH = "src/main/resources/output.txt";
    private final ScheduledExecutorService scheduler;

    public TimerController() {
        scheduler = Executors.newScheduledThreadPool(2);
    }

    public void start() {
        clearFileContents();

        scheduler.scheduleAtFixedRate(() -> writeToFile(Timer.getTimeTask()), 0, 1000, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(() -> writeToFile(Timer.getMessageTask()), 5000, 5000, TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void shutdown() {
        System.out.println("Shutdown initiated...");
        printFileContents();
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

    private static synchronized void writeToFile(String message) {
        try {
            Files.write(Path.of(FILE_PATH), (message + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printFileContents() {
        try {
            Files.lines(Path.of(FILE_PATH), StandardCharsets.UTF_8).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearFileContents() {
        try {
            Files.write(Path.of(FILE_PATH), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
