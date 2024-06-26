package org.example.timer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code Timer} class provides methods to get the current time formatted as a string and to get a message indicating
 * that 5 seconds have passed.
 */
public class Timer {

    /**
     * Returns the current time formatted as a string in the "HH:mm:ss" pattern.
     *
     * @return the formatted current time as a string
     */
    public static String getTimeTask() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }

    /**
     * Returns a message indicating that 5 seconds have passed.
     *
     * @return the message "5 seconds have passed"
     */
    public static String getMessageTask() {
        return "5 seconds have passed";
    }
}
