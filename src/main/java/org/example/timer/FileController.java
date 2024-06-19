package org.example.timer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileController {
    private static final String FILE_PATH = "src/main/resources/output.txt";

    public static synchronized void writeToFile(String message) {
        try {
            Files.write(Path.of(FILE_PATH), (message + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printFileContents() {
        try {
            Files.lines(Path.of(FILE_PATH), StandardCharsets.UTF_8).forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFileContents() {
        try {
            Files.write(Path.of(FILE_PATH), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
