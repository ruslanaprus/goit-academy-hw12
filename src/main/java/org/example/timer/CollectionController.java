package org.example.timer;

import java.util.ArrayList;
import java.util.List;

public class CollectionController {
    private static final List<String> dataList = new ArrayList<>();

    public static void addToDataList(String data) {
        synchronized (dataList) {
            dataList.add(data);
        }
    }

    public static void readDataFromList() {
        synchronized (dataList) {
            System.out.println("Printing dataList:");
            dataList.forEach(System.out::println);
        }
    }
}
