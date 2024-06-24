package org.example.timer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileDataHandler implements DataHandler {
    private static final String FILE_PATH = "src/main/resources/output.txt";
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public void writeData(String message) {
        lock.writeLock().lock();
        try {
            Files.write(Path.of(FILE_PATH), (message + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
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
            Files.lines(Path.of(FILE_PATH), StandardCharsets.UTF_8).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

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
