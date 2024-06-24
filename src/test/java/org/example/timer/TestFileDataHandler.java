package org.example.timer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestFileDataHandler {

    private static final String TEST_OUTPUT_FILE = "src/test/java/org/example/timer/testOutput.txt";
    private ScheduledExecutorService testScheduler;

    @BeforeEach
    public void setUp() {
        testScheduler = Executors.newScheduledThreadPool(2);
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (testScheduler != null) {
            testScheduler.shutdown();
            try {
                if (!testScheduler.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                    testScheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                testScheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Test
    public void testStart_WithFileDataHandler() throws IOException {
        Files.write(Path.of(TEST_OUTPUT_FILE), new byte[0], StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);

        FileDataHandler fileDataHandler = mock(FileDataHandler.class);
        doAnswer(invocation -> {
            String message = invocation.getArgument(0);
            Files.write(Path.of(TEST_OUTPUT_FILE), (message + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return null;
        }).when(fileDataHandler).writeData(anyString());

        SchedulerController schedulerController = new SchedulerController(fileDataHandler, testScheduler);

        testScheduler.schedule(() -> {
            schedulerController.start();
        }, 1, TimeUnit.SECONDS);

        awaitCompletion();

        verify(fileDataHandler, atLeastOnce()).writeData(anyString());

        List<String> lines = Files.readAllLines(Path.of(TEST_OUTPUT_FILE));
        assertFalse(lines.isEmpty());

        System.out.println("Data written to file:");
        lines.forEach(System.out::println);
    }

    private void awaitCompletion() {
        try {
            testScheduler.awaitTermination(12000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
