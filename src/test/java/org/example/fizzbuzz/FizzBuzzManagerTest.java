package org.example.fizzbuzz;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class FizzBuzzManagerTest {
    @Test
    public void testConcurrencyWithN30() throws InterruptedException {
        int n = 30;
        FizzBuzzManager fizzBuzzManager = new FizzBuzzManager(n);
        FizzBuzzExecutor fizzBuzzExecutor = new FizzBuzzExecutor(fizzBuzzManager);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.submit(fizzBuzzManager.createFizzTask());
        executor.submit(fizzBuzzManager.createBuzzTask());
        executor.submit(fizzBuzzManager.createFizzBuzzTask());
        executor.submit(fizzBuzzManager.createNumberTask());

        executor.shutdown();
        executor.awaitTermination(n * 100 + 1000, TimeUnit.MILLISECONDS);

        List<String> outputList = fizzBuzzExecutor.collectOutput();
        List<String> expectedOutput = Arrays.asList("1", "2", "fizz", "4", "buzz", "fizz", "7", "8", "fizz",
                "buzz", "11", "fizz", "13", "14", "fizzbuzz", "16", "17", "fizz", "19", "buzz", "fizz", "22",
                "23", "fizz", "buzz", "26", "fizz", "28", "29", "fizzbuzz");
        System.out.println(outputList);

        assertEquals(expectedOutput, outputList);
    }

    @Test
    public void testConcurrencyWithN100() throws InterruptedException{
        int n = 100;
        FizzBuzzManager fizzBuzzManager = new FizzBuzzManager(n);
        FizzBuzzExecutor fizzBuzzExecutor = new FizzBuzzExecutor(fizzBuzzManager);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.submit(fizzBuzzManager.createFizzTask());
        executor.submit(fizzBuzzManager.createBuzzTask());
        executor.submit(fizzBuzzManager.createFizzBuzzTask());
        executor.submit(fizzBuzzManager.createNumberTask());

        executor.shutdown();
        executor.awaitTermination(n * 100 + 1000, TimeUnit.MILLISECONDS);

        List<String> outputList = fizzBuzzExecutor.collectOutput();
        System.out.println(outputList);

        assertEquals(100, outputList.size());
        validateOutput(outputList);
    }

    @Test
    public void testEmptyQueueWithN0() throws InterruptedException {
        int n = 0;
        FizzBuzzManager fizzBuzzManager = new FizzBuzzManager(n);
        FizzBuzzExecutor fizzBuzzExecutor = new FizzBuzzExecutor(fizzBuzzManager);

        fizzBuzzExecutor.start();
        List<String> outputList = fizzBuzzExecutor.collectOutput();
        assertTrue(outputList.isEmpty());
    }

    private void validateOutput(List<String> outputList) {
        for (int i = 1; i <= outputList.size(); i++) {
            String expected;
            if (i % 3 == 0 && i % 5 == 0) {
                expected = "fizzbuzz";
            } else if (i % 3 == 0) {
                expected = "fizz";
            } else if (i % 5 == 0) {
                expected = "buzz";
            } else {
                expected = String.valueOf(i);
            }
            assertEquals(expected, outputList.get(i - 1));
        }
    }

}