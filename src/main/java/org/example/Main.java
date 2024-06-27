package org.example;

import org.example.timer.CollectionDataHandler;
import org.example.timer.FileDataHandler;
import org.example.timer.SchedulerController;

/**
 * The {@code Main} class contains the entry point of the application.
 */
public class Main {
    public static void main(String[] args) {
        SchedulerController dataIntoList = new SchedulerController(new CollectionDataHandler(true));
        dataIntoList.start();
        SchedulerController dataIntoFile = new SchedulerController(new FileDataHandler(false));
        dataIntoFile.start();
    }
}