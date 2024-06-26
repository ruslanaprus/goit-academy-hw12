package org.example.fizzbuzz;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Executes the FizzBuzzManager in a multi-threaded environment, managing the collection and output of results.
 */
public class FizzBuzzExecutor {
    private final FizzBuzzManager fizzBuzzManager;

    /**
     * Constructs a FizzBuzzExecutor with the specified FizzBuzzManager.
     *
     * @param fizzBuzzManager the FizzBuzzManager to be executed
     */
    public FizzBuzzExecutor(FizzBuzzManager fizzBuzzManager) {
        this.fizzBuzzManager = fizzBuzzManager;
    }

    /**
     * Collects the output results from the FizzBuzzManager.
     *
     * @return a list of output strings
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
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

    /**
     * Starts the execution of FizzBuzz tasks using four threads.
     * The tasks include FizzTask, BuzzTask, FizzBuzzTask, and NumberTask.
     */
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
