package org.example.timer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Timer {

    public static String getTimeTask() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
      //  return now.format(formatter);
        return "" + LocalTime.now();
    }

    public static String getMessageTask() {
        return "5 seconds have passed" + LocalTime.now();
    }
}
