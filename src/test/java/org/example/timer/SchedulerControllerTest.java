package org.example.timer;

import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SchedulerControllerTest {

    private static final String TEST_OUTPUT_FILE = "src/test/java/org/example/timer/testOutput.txt";
    private ScheduledExecutorService testScheduler;

    @BeforeEach
    public void setUp() {
        testScheduler = Executors.newScheduledThreadPool(2);
    }

    @AfterEach
    public void tearDown() throws IOException {
        if (testScheduler != null && !testScheduler.isShutdown()) {
            testScheduler.shutdownNow();
        }
    }

    @Test
    public void testStart_WithCollectionDataHandler() throws InterruptedException {
        CollectionDataHandler collectionDataHandler = new CollectionDataHandler();
        SchedulerController schedulerController = new SchedulerController(collectionDataHandler, testScheduler);

        schedulerController.start();

        Thread.sleep(11000);

        List<String> dataList = CollectionDataHandler.getDataList();
        assertFalse(dataList.isEmpty());

        System.out.println("Data written to CollectionDataHandler:");
        dataList.forEach(System.out::println);

        try {
            Method shutdownMethod = SchedulerController.class.getDeclaredMethod("shutdown");
            shutdownMethod.setAccessible(true);
            shutdownMethod.invoke(schedulerController);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to invoke shutdown method via reflection");
        }
    }

    @Test
    public void testStart_WithFileDataHandler() throws IOException, InterruptedException {
        Files.write(Path.of(TEST_OUTPUT_FILE), new byte[0], StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);

        FileDataHandler fileDataHandler = new TestFileDataHandler(TEST_OUTPUT_FILE);
        SchedulerController schedulerController = new SchedulerController(fileDataHandler, testScheduler);

        schedulerController.start();

        Thread.sleep(11000);

        List<String> lines = Files.readAllLines(Path.of(TEST_OUTPUT_FILE));
        assertFalse(lines.isEmpty());

        System.out.println("Data written to file:");
        lines.forEach(System.out::println);

        try {
            Method shutdownMethod = SchedulerController.class.getDeclaredMethod("shutdown");
            shutdownMethod.setAccessible(true);
            shutdownMethod.invoke(schedulerController);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to invoke shutdown method via reflection");
        }
    }

    @Test
    public void testShutdown() throws InterruptedException {
        CollectionDataHandler collectionDataHandler = new CollectionDataHandler();
        SchedulerController schedulerController = new SchedulerController(collectionDataHandler, testScheduler);

        schedulerController.start();

        try {
            Method shutdownMethod = SchedulerController.class.getDeclaredMethod("shutdown");
            shutdownMethod.setAccessible(true);
            shutdownMethod.invoke(schedulerController);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to invoke shutdown method via reflection");
        }

        assertTrue(testScheduler.isShutdown());

        List<String> dataList = CollectionDataHandler.getDataList();
        System.out.println("Data in CollectionDataHandler after shutdown:");
        dataList.forEach(System.out::println);
    }

    @Test
    public void testShutdown_Forcing() throws InterruptedException {
        CollectionDataHandler collectionDataHandler = new CollectionDataHandler();
        SchedulerController schedulerController = new SchedulerController(collectionDataHandler, testScheduler);

        schedulerController.start();

        testScheduler.awaitTermination(1, TimeUnit.SECONDS);

        try {
            Method shutdownMethod = SchedulerController.class.getDeclaredMethod("shutdown");
            shutdownMethod.setAccessible(true);
            shutdownMethod.invoke(schedulerController);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to invoke shutdown method via reflection");
        }

        assertTrue(testScheduler.isShutdown());

        List<String> dataList = CollectionDataHandler.getDataList();
        System.out.println("Data in CollectionDataHandler after forced shutdown:");
        dataList.forEach(System.out::println);
    }

    private static class TestFileDataHandler extends FileDataHandler {
        private final String testFilePath;
        private final ReadWriteLock lock = new ReentrantReadWriteLock();


        public TestFileDataHandler(String testFilePath) {
            this.testFilePath = testFilePath;
        }

        @Override
        public void writeData(String message) {
            lock.writeLock().lock();
            try {
                Files.write(Path.of(testFilePath), (message + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void readData() {
            lock.readLock().lock();
            try {
                Files.lines(Path.of(testFilePath), StandardCharsets.UTF_8).forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.readLock().unlock();
            }
        }
    }
}
