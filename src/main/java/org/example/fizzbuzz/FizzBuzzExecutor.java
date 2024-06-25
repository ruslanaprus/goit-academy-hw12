package org.example.fizzbuzz;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FizzBuzzExecutor {
    private final FizzBuzzManager fizzBuzzManager;

    public FizzBuzzExecutor(FizzBuzzManager fizzBuzzManager) {
        this.fizzBuzzManager = fizzBuzzManager;
    }

    public List<String> collectOutput() throws InterruptedException {
        List<String> outputList = new CopyOnWriteArrayList<>();

        try {
            while (!Thread.currentThread().isInterrupted()) {
                String output = fizzBuzzManager.getOutputQueue().poll(1, TimeUnit.SECONDS);
                if (output != null) {
                    outputList.add(output);
                } else {
                    if (fizzBuzzManager.getCurrent().get() > fizzBuzzManager.getN() && fizzBuzzManager.getOutputQueue().isEmpty()) {
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        }
        return List.copyOf(outputList);
    }

    public void start() {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.submit(fizzBuzzManager.createFizzTask());
        executor.submit(fizzBuzzManager.createBuzzTask());
        executor.submit(fizzBuzzManager.createFizzBuzzTask());
        executor.submit(fizzBuzzManager.createNumberTask());

        executor.shutdown();
        try {
            if (!executor.awaitTermination(fizzBuzzManager.getN() * 100 + 1000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
}
