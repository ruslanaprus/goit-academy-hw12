package org.example.timer;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TestCollectionDataHandler {

    private SchedulerController schedulerController;
    private CollectionDataHandler dataHandler;
    private ScheduledExecutorService scheduler;

    @BeforeEach
    public void setUp() {
        dataHandler = new CollectionDataHandler();
        scheduler = Executors.newScheduledThreadPool(2);
        schedulerController = new SchedulerController(dataHandler, scheduler);
        schedulerController.start();
    }

    @AfterEach
    public void tearDown() {
        invokePrivateShutdown();
    }


    @Test
    public void testSchedulerWritesTimeTask() {
        Awaitility.await().atMost(Duration.ofSeconds(2))
                .untilAsserted(() -> {
                    List<String> dataList = CollectionDataHandler.getDataList();
                    assertFalse(dataList.isEmpty(), "Data list should not be empty");
                    assertTrue(dataList.stream().anyMatch(data -> data.matches("\\d{2}:\\d{2}:\\d{2}")),
                            "Data list should contain a time task entry");
                });
    }

    @Test
    public void testSchedulerWritesMessageTask() {
        Awaitility.await().atMost(Duration.ofSeconds(6))
                .untilAsserted(() -> {
                    List<String> dataList = CollectionDataHandler.getDataList();
                    assertTrue(dataList.contains("5 seconds have passed"),
                            "Data list should contain the message task entry");
                });
    }

    @Test
    public void testSchedulerWritesBothTasks() {
        Awaitility.await().atMost(Duration.ofSeconds(6))
                .untilAsserted(() -> {
                    List<String> dataList = CollectionDataHandler.getDataList();
                    assertFalse(dataList.isEmpty(), "Data list should not be empty");
                    assertTrue(dataList.stream().anyMatch(data -> data.matches("\\d{2}:\\d{2}:\\d{2}")),
                            "Data list should contain a time task entry");
                    assertTrue(dataList.contains("5 seconds have passed"),
                            "Data list should contain the message task entry");
                });
    }

    @Test
    public void testShutdown() {
        invokePrivateShutdown();

        assertTrue(scheduler.isShutdown(), "Scheduler should be shut down");

        Awaitility.await().atMost(Duration.ofSeconds(6))
                .untilAsserted(() -> {
                    List<String> dataList = CollectionDataHandler.getDataList();
                    assertNotNull(dataList, "Data list should not be null");
                });
    }

    private void invokePrivateShutdown() {
        try {
            Method shutdownMethod = SchedulerController.class.getDeclaredMethod("shutdown");
            shutdownMethod.setAccessible(true);
            shutdownMethod.invoke(schedulerController);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke shutdown method", e);
        }
    }
}
