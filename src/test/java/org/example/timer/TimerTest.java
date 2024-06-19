package org.example.timer;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class TimerTest {

    @Test
    public void testTimerTasks() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Runnable timeTask = () -> {
            System.out.println("timeTask executed");
        };

        Runnable messageTask = () -> {
            System.out.println("5 seconds have passed");
        };

        scheduler.scheduleAtFixedRate(timeTask, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(messageTask, 5, 5, TimeUnit.SECONDS);

        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            String output = outContent.toString();
            assertTrue(output.contains("timeTask executed"));
            assertTrue(output.contains("5 seconds have passed"));
        });

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

}