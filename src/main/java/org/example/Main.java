package org.example;

import org.example.timer.CollectionController;
import org.example.timer.FileController;
import org.example.timer.TimerController;

public class Main {
    public static void main(String[] args) {
        TimerController controller = new TimerController(new CollectionController());
        controller.start();
        TimerController controller2 = new TimerController(new FileController());
        controller2.start();
    }
}