package org.example.timer;

import java.util.ArrayList;
import java.util.List;

public class CollectionController implements DataController {
    private static final List<String> dataList = new ArrayList<>();

    @Override
    public void writeData(String data) {
        synchronized (dataList) {
            dataList.add(data);
        }
    }

    @Override
    public void readData() {
        synchronized (dataList) {
            System.out.println("Printing dataList:");
            dataList.forEach(System.out::println);
        }
    }
}
