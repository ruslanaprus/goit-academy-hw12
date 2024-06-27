package org.example.timer;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CollectionDataHandler} class implements the {@code DataHandler} interface to handle data using an in-memory
 * collection.
 */
public class CollectionDataHandler implements DataHandler {
    private static final List<String> dataList = new ArrayList<>();
    private boolean realTimeConsoleOutput;

    /**
     * Constructs a new {@code CollectionDataHandler} with the option to enable real-time console output.
     *
     * @param realTimeConsoleOutput true to enable real-time console output; false otherwise
     */
    public CollectionDataHandler(boolean realTimeConsoleOutput) {
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
     * Writes the specified data to the in-memory collection and optionally prints it to the console.
     *
     * @param data the data to be written
     */
    @Override
    public void writeData(String data) {
        synchronized (dataList) {
            dataList.add(data);
            if (realTimeConsoleOutput) {
                System.out.println(data);
            }
        }
    }

    /**
     * Reads the data from the in-memory collection and prints it to the console.
     */
    @Override
    public void readData() {
        synchronized (dataList) {
            System.out.println("Printing dataList:");
            dataList.forEach(System.out::println);
        }
    }

    /**
     * Returns an unmodifiable copy of the data list.
     *
     * @return the data list
     */
    public static List<String> getDataList() {
        synchronized (dataList) {
            return List.copyOf(dataList);
        }
    }
}
