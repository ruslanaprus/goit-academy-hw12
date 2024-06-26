package org.example.fizzbuzz;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the execution of FizzBuzz tasks in a multi-threaded environment, allowing concurrent generation of Fizz, Buzz, FizzBuzz, and number outputs.
 * This program outputs to the console a sequence of numbers from 1 to n, with the following replacements:
 * <ul>
 *     <li>If a number is divisible by 3, "fizz" is output.</li>
 *     <li>If a number is divisible by 5, "buzz" is output.</li>
 *     <li>If a number is divisible by both 3 and 5, "fizzbuzz" is output.</li>
 * </ul>
 * For example, for n = 15, the expected output is: 1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz.
 * The program uses four threads to perform this task:
 * <ul>
 *     <li>Thread "fizz" checks if a number is divisible by 3 and outputs "fizz".</li>
 *     <li>Thread "buzz" checks if a number is divisible by 5 and outputs "buzz".</li>
 *     <li>Thread "fizzbuzz" checks if a number is divisible by both 3 and 5 and outputs "fizzbuzz".</li>
 *     <li>Thread "number" outputs the next number if it is not divisible by either 3 or 5.</li>
 * </ul>
 */
public class FizzBuzzManager {
    private final int n;
    private final AtomicInteger current = new AtomicInteger(1);
    private final BlockingQueue<String> outputQueue = new LinkedBlockingQueue<>();

    /**
     * Constructs a FizzBuzzManager to run tasks up to the given number.
     *
     * @param n the maximum number to count up to
     */
    public FizzBuzzManager(int n) {
        this.n = n;
    }

    /**
     * Abstract base class for tasks that produce output based on divisibility rules.
     */
    private abstract class Task implements Runnable {
        /**
         * The divisor for the task.
         */
        protected final int divisor;
        /**
         * The output string for the task.
         */
        protected final String output;
        /**
         * Indicates if the task is a FizzBuzz task.
         */
        protected final boolean isFizzBuzzTask;

        /**
         * Constructs a new Task with the specified parameters.
         *
         * @param divisor        the divisor for the task
         * @param output         the output string for the task
         * @param isFizzBuzzTask flag indicating if this is a FizzBuzz task
         */
        public Task(int divisor, String output, boolean isFizzBuzzTask) {
            this.divisor = divisor;
            this.output = output;
            this.isFizzBuzzTask = isFizzBuzzTask;
        }

        @Override
        public void run() {
            while (true) {
                int num;
                synchronized (current) {
                    num = current.get();
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
    }

    /**
     * Task that produces "fizz" output for numbers divisible by 3.
     */
    private class FizzTask extends Task {
        /**
         * Constructs a new FizzTask.
         */
        public FizzTask() {
            super(3, "fizz", false);
        }
    }

    /**
     * Task that produces "buzz" output for numbers divisible by 5.
     */
    private class BuzzTask extends Task {
        /**
         * Constructs a new BuzzTask.
         */
        public BuzzTask() {
            super(5, "buzz", false);
        }
    }

    /**
     * Task that produces "fizzbuzz" output for numbers divisible by both 3 and 5.
     */
    private class FizzBuzzTask extends Task {
        /**
         * Constructs a new FizzBuzzTask.
         */
        public FizzBuzzTask() {
            super(0, "fizzbuzz", true);
        }
    }

    /**
     * Task that produces number output for numbers not divisible by 3 or 5.
     */
    private class NumberTask extends Task {
        /**
         * Constructs a new NumberTask.
         */
        public NumberTask() {
            super(0, "num", false);
        }

        @Override
        public void run() {
            while (true) {
                int num;
                synchronized (current) {
                    num = current.get();
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
    }

    /**
     * Creates a FizzTask runnable.
     *
     * @return a runnable that produces "fizz" output
     */
    Runnable createFizzTask() {
        return new FizzTask();
    }

    /**
     * Creates a BuzzTask runnable.
     *
     * @return a runnable that produces "buzz" output
     */
    Runnable createBuzzTask() {
        return new BuzzTask();
    }

    /**
     * Creates a FizzBuzzTask runnable.
     *
     * @return a runnable that produces "fizzbuzz" output
     */
    Runnable createFizzBuzzTask() {
        return new FizzBuzzTask();
    }

    /**
     * Creates a NumberTask runnable.
     *
     * @return a runnable that produces number output
     */
    Runnable createNumberTask() {
        return new NumberTask();
    }

    /**
     * Gets the maximum number to count up to.
     *
     * @return the maximum number
     */
    public int getN() {
        return n;
    }

    /**
     * Gets the current number being processed.
     *
     * @return the current number
     */
    public AtomicInteger getCurrent() {
        return current;
    }

    /**
     * Gets the queue holding the output results.
     *
     * @return the output queue
     */
    public BlockingQueue<String> getOutputQueue() {
        return outputQueue;
    }
}
