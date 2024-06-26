package org.example.timer;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CollectionDataHandler} class implements the {@code DataHandler} interface to handle data using an in-memory
 * collection.
 */
public class CollectionDataHandler implements DataHandler {
    private static final List<String> dataList = new ArrayList<>();

    /**
     * Writes the specified data to the in-memory collection.
     *
     * @param data the data to be written
     */
    @Override
    public void writeData(String data) {
        synchronized (dataList) {
            dataList.add(data);
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
