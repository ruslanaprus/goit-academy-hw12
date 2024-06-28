package org.example;

import org.example.timer.CollectionDataHandler;
import org.example.timer.FileDataHandler;
import org.example.timer.SchedulerController;

import java.util.Scanner;

/**
 * The {@code Main} class contains the entry point of the application.
 */
public class Main {
    public static void main(String[] args) {
        SchedulerController dataIntoList = new SchedulerController(new CollectionDataHandler(true));
        dataIntoList.start();

        System.out.println("Press Enter to read data and exit...");
        new Scanner(System.in).nextLine();

        dataIntoList.readData();

        dataIntoList.shutdown();

        SchedulerController dataIntoFile = new SchedulerController(new FileDataHandler(false));
        dataIntoFile.start();
    }
}