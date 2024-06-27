package org.example.timer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The {@code FileDataHandler} class implements the {@code DataHandler} interface to handle data using a file.
 */
public class FileDataHandler implements DataHandler {
    private static final String FILE_PATH = "src/main/resources/output.txt";
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private boolean realTimeConsoleOutput;

    /**
     * Constructs a new {@code FileDataHandler} with the option to enable real-time console output.
     *
     * @param realTimeConsoleOutput true to enable real-time console output; false otherwise
     */
    public FileDataHandler(boolean realTimeConsoleOutput) {
        this.realTimeConsoleOutput = realTimeConsoleOutput;
    }

    /**
     * Enables or disables real-time console output.
     *
     * @param enable true to enable; false to disable
     */
    @Override
    public void enableRealTimeConsoleOutput(boolean enable) {
        this.realTimeConsoleOutput = enable;
    }

    /**
     * Writes the specified data to the file and optionally prints it to the console.
     *
     * @param message the data to be written
     */
    @Override
    public void writeData(String message) {
        lock.writeLock().lock();
        try {
            Files.write(Path.of(FILE_PATH), (message + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            if (realTimeConsoleOutput) {
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Reads the data from the file and prints it to the console.
     */
    @Override
    public void readData() {
        lock.readLock().lock();
        try {
            Files.lines(Path.of(FILE_PATH), StandardCharsets.UTF_8).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Clears the data in the file.
     */
    public void clearData() {
        lock.writeLock().lock();
        try {
            Files.write(Path.of(FILE_PATH), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }
}