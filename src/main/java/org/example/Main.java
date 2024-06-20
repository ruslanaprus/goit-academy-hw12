package org.example;

import org.example.timer.CollectionDataHandler;
import org.example.timer.FileDataHandler;
import org.example.timer.SchedulerController;

public class Main {
    public static void main(String[] args) {
        SchedulerController dataIntoList = new SchedulerController(new CollectionDataHandler());
        dataIntoList.start();
        SchedulerController dataIntoFile = new SchedulerController(new FileDataHandler());
        dataIntoFile.start();
    }
}