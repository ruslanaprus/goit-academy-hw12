package org.example.fizzbuzz;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FizzBuzzManager {
    private final int n;
    private final AtomicInteger current = new AtomicInteger(1);
    private final BlockingQueue<String> outputQueue = new LinkedBlockingQueue<>();

    public FizzBuzzManager(int n) {
        this.n = n;
    }

    private abstract class Task implements Runnable {
        protected final int divisor;
        protected final String output;
        protected final boolean isFizzBuzzTask;

        public Task(int divisor, String output, boolean isFizzBuzzTask) {
            this.divisor = divisor;
            this.output = output;
            this.isFizzBuzzTask = isFizzBuzzTask;
        }

        @Override
        public void run() {
            while (true) {
                int num = current.get();
                if (num > n) return;
                if ((isFizzBuzzTask && num % 3 == 0 && num % 5 == 0) ||
                        (!isFizzBuzzTask && num % divisor == 0 && num % (divisor == 3 ? 5 : 3) != 0)) {
                    try {
                        outputQueue.put(output.equals(String.valueOf(num)) ? String.valueOf(num) : output);
                        current.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    private class FizzTask extends Task {
        public FizzTask() {
            super(3, "fizz", false);
        }
    }

    private class BuzzTask extends Task {
        public BuzzTask() {
            super(5, "buzz", false);
        }
    }

    private class FizzBuzzTask extends Task {
        public FizzBuzzTask() {
            super(0, "fizzbuzz", true);
        }
    }

    private class NumberTask extends Task {
        public NumberTask() {
            super(0, "num", false);
        }

        @Override
        public void run() {
            while (true) {
                int num = current.get();
                if (num > n) return;
                if (num % 3 != 0 && num % 5 != 0) {
                    try {
                        outputQueue.put(String.valueOf(num));
                        current.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    Runnable createFizzTask() {
        return new FizzTask();
    }

    Runnable createBuzzTask() {
        return new BuzzTask();
    }

    Runnable createFizzBuzzTask() {
        return new FizzBuzzTask();
    }

    Runnable createNumberTask() {
        return new NumberTask();
    }

    public int getN() {
        return n;
    }

    public AtomicInteger getCurrent() {
        return current;
    }

    public BlockingQueue<String> getOutputQueue() {
        return outputQueue;
    }
}
