package org.example.fizzbuzz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FizzBuzzExecutorTest {
    private FizzBuzzManager fizzBuzzManager;
    private FizzBuzzExecutor fizzBuzzExecutor;
    private BlockingQueue<String> outputQueue;
    private AtomicInteger current;

    @BeforeEach
    void setUp() {
        fizzBuzzManager = mock(FizzBuzzManager.class);
        fizzBuzzExecutor = new FizzBuzzExecutor(fizzBuzzManager);
        outputQueue = new LinkedBlockingQueue<>();
        current = new AtomicInteger(0);

        when(fizzBuzzManager.getOutputQueue()).thenReturn(outputQueue);
        when(fizzBuzzManager.getCurrent()).thenReturn(current);
        when(fizzBuzzManager.getN()).thenReturn(15);
    }

    @Test
    void testStart() throws InterruptedException {
        Runnable fizzTask = mock(Runnable.class);
        Runnable buzzTask = mock(Runnable.class);
        Runnable fizzBuzzTask = mock(Runnable.class);
        Runnable numberTask = mock(Runnable.class);

        when(fizzBuzzManager.createFizzTask()).thenReturn(fizzTask);
        when(fizzBuzzManager.createBuzzTask()).thenReturn(buzzTask);
        when(fizzBuzzManager.createFizzBuzzTask()).thenReturn(fizzBuzzTask);
        when(fizzBuzzManager.createNumberTask()).thenReturn(numberTask);

        fizzBuzzExecutor.start();

        verify(fizzBuzzManager).createFizzTask();
        verify(fizzBuzzManager).createBuzzTask();
        verify(fizzBuzzManager).createFizzBuzzTask();
        verify(fizzBuzzManager).createNumberTask();
        verify(fizzTask).run();
        verify(buzzTask).run();
        verify(fizzBuzzTask).run();
        verify(numberTask).run();
    }

    @Test
    void testCollectOutput() throws InterruptedException {
        outputQueue.put("1");
        outputQueue.put("2");
        outputQueue.put("Fizz");
        outputQueue.put("4");
        outputQueue.put("Buzz");
        outputQueue.put("Fizz");
        outputQueue.put("7");
        outputQueue.put("8");
        outputQueue.put("Fizz");
        outputQueue.put("Buzz");
        outputQueue.put("11");
        outputQueue.put("Fizz");
        outputQueue.put("13");
        outputQueue.put("14");
        outputQueue.put("FizzBuzz");
        current.set(16);

        List<String> output = fizzBuzzExecutor.collectOutput();

        System.out.println("Collected Output:");
        System.out.println(output);

        assertEquals(15, output.size());
        assertEquals(List.of("1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz", "Buzz", "11", "Fizz", "13", "14", "FizzBuzz"), output);
    }

    @Test
    void testCollectOutputWithInterrupt() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                List<String> output = fizzBuzzExecutor.collectOutput();
                System.out.println("Output collected: " + output);
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted.");
            }
        });

        thread.start();

        interruptThreadAfterDelay(thread, 2000);

        thread.join(3000);

        assertFalse(thread.isAlive(), "Thread should have completed.");
    }

    private void interruptThreadAfterDelay(Thread thread, long delayMillis) {
        new Thread(() -> {
            try {
                Thread.sleep(delayMillis);
                thread.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}