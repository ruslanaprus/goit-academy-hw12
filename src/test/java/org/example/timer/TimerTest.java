package org.example.timer;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TimerTest {

    @Test
    public void testGetTimeTask() {
        String time = Timer.getTimeTask();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime parsedTime = LocalTime.parse(time, formatter);
        assertEquals(time, parsedTime.format(formatter));
    }

    @Test
    public void testGetMessageTask() {
        String message = Timer.getMessageTask();
        assertEquals("5 seconds have passed", message);
    }

    @Test
    public void testSchedulerTasks() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Runnable timeTask = () -> {
            System.out.println(Timer.getTimeTask());
        };

        Runnable messageTask = () -> {
            System.out.println(Timer.getMessageTask());
        };

        scheduler.scheduleAtFixedRate(timeTask, 0, 1000, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(messageTask, 5000, 5000, TimeUnit.MILLISECONDS);

        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            String output = outContent.toString();
            assertTrue(output.contains("5 seconds have passed"));
            assertTrue(output.lines().filter(line -> line.matches("\\d{2}:\\d{2}:\\d{2}")).count() >= 5);
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
