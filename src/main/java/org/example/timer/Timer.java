package org.example.timer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Timer {
    private static final String FILE_PATH = "src/main/resources/output.txt";

    public static Runnable getTimeTask() {
        return () -> {
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            writeToFile(now.format(formatter));
        };
    }

    public static Runnable getMessageTask() {
        return () -> writeToFile("5 seconds have passed");
    }

    private static void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    private static void writeToFile(String filePath, String message) {
//        Path path = Path.of(filePath);
//        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE)) {
//            writer.write(message);
//            writer.newLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void printFileContents() {
        Path path = Path.of(FILE_PATH);
        try {
            Files.lines(path, StandardCharsets.UTF_8).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
