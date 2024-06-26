# Concurrent Time Tracker and FizzBuzz Solver

## Overview
This project, created as a solution for the GoIT Academy Module 12 [Multithreading] hometask, utilises multithreading techniques while implementing a real-time tracker and the classic FizzBuzz problem. It contains:
- Task 1: [Timer](#timer) - a time tracker that accurately tracks the time elapsed since the start of the program, outputting messages at regular intervals - every 5 seconds;
- Task 2: [FizzBuzz](#fizzbuzz) - an implementation of the classic FizzBuzz problem that leverages Java's concurrency utilities to efficiently manage and execute tasks in parallel, ensuring thread-safe operations and streamlined output handling.

Designed with extensibility in mind, the project supports various data storage methods, making it adaptable for diverse use cases.

## Timer

- `org.example.timer`: Contains the core classes and interfaces for the timer functionality.

### Classes and Interfaces

### `Timer` class
- Provides methods to get the current time formatted as a string and to get a message indicating that 5 seconds have passed.

#### Methods
- `getTimeTask()`: Returns the current time formatted as a string in "HH:mm:ss" pattern.
- `getMessageTask()`: Returns a message indicating that 5 seconds have passed.

### `DataHandler` interface
- Defines methods for writing and reading data.

#### Methods
- `writeData(String data)`: Writes the specified data.
- `readData()`: Reads the data.

### `CollectionDataHandler` class
- Implements `DataHandler` using an in-memory collection (`List`).

#### Methods
- `writeData(String data)`: Writes the specified data to the in-memory collection.
- `readData()`: Reads the data from the in-memory collection.
- `getDataList()`: Returns an unmodifiable copy of the data list.

### `FileDataHandler` class
- Implements `DataHandler` using a file for data storage.

#### Methods
- `writeData(String message)`: Writes the specified data to a file.
- `readData()`: Reads the data from the file.
- `clearData()`: Clears the data in the file.

### `SchedulerController` class
- Manages the scheduling of tasks to write data at regular intervals.

#### Methods
- `start()`: Starts the scheduler to write data at regular intervals.
- `shutdown()`: Initiates shutdown of the scheduler and reads data.

## Project Structure and Extensibility
The project is designed with a clear separation of concerns, using interfaces and implementations to handle data storage. The `DataHandler` interface defines the contract for writing and reading data, while the `CollectionDataHandler` and `FileDataHandler` classes provide specific implementations of this interface.

This design makes it easy to extend the application by adding more classes that implement the `DataHandler` interface. For example, you can add a class to save data to a database, a remote server, or any other storage system. Simply implement the `DataHandler` interface and provide the necessary logic for the `writeData` and `readData` methods.

### Example of Adding a New Data Handler
To add a new data handler, such as `DatabaseDataHandler`, you would do the following:

Implement the `DataHandler` interface.
Provide the logic for saving and retrieving data in the writeData and readData methods.
```java
public class DatabaseDataHandler implements DataHandler {

    @Override
    public void writeData(String data) {
        // Logic to save data to a database
    }

    @Override
    public void readData() {
        // Logic to read data from the database
    }
}
```
## Usage

Start the time scheduler and write data to either an in-memory collection or a file.

```java
        // Example using CollectionDataHandler
        SchedulerController dataIntoList = new SchedulerController(new CollectionDataHandler());
        dataIntoList.start();

        // Example using FileDataHandler
        SchedulerController dataIntoFile = new SchedulerController(new FileDataHandler());
        dataIntoFile.start();
```
### Example Output
When running the application, you will see the current time was added every second and every 5 second - a message:

```bash
12:00:00
12:00:01
12:00:02
12:00:03
12:00:04
5 seconds have passed
12:00:05
12:00:06
...
```

## FizzBuzz

This project is a concurrent implementation of the classic FizzBuzz problem. It leverages Java's concurrency utilities to efficiently execute multiple tasks in parallel, ensuring thread-safe operations and streamlined output handling.

The FizzBuzz problem involves printing numbers from 1 to `n`, with replacements for numbers divisible by 3, 5, and both:
- Numbers divisible by 3 are replaced with "fizz".
- Numbers divisible by 5 are replaced with "buzz".
- Numbers divisible by both 3 and 5 are replaced with "fizzbuzz".

### Features

- **Concurrent Execution**: Utilizes an `ExecutorService` with a fixed thread pool to run multiple tasks concurrently.
- **Atomic Coordination**: Employs an `AtomicInteger` (`current`) to keep track of the current number being processed, ensuring thread-safe increments.
- **Blocking Queue**: Uses a `BlockingQueue` (`outputQueue`) to manage and store output strings (`fizz`, `buzz`, `fizzbuzz`, and numbers).
- **Task Abstraction**: Defines an abstract `Task` class to encapsulate common behavior of different tasks (`FizzTask`, `BuzzTask`, `FizzBuzzTask`, `NumberTask`).
- **Task Implementation**:
    - `FizzTask`: Outputs "fizz" for numbers divisible by 3 but not by 5.
    - `BuzzTask`: Outputs "buzz" for numbers divisible by 5 but not by 3.
    - `FizzBuzzTask`: Outputs "fizzbuzz" for numbers divisible by both 3 and 5.
    - `NumberTask`: Outputs the number itself if it is not divisible by either 3 or 5.
- **Output Handling**: A separate task continuously polls the `outputQueue` and prints the results to the console.
- **Graceful Termination**: Ensures tasks terminate when the current number exceeds the specified limit (`n`). Uses `ExecutorService.shutdown()` and `awaitTermination` for clean shutdown.


### The project consists of two main classes:
- `FizzBuzzManager`: Manages the tasks for generating FizzBuzz outputs.
- `FizzBuzzExecutor`: Executes the FizzBuzzManager in a multi-threaded environment and collects the results.


### `FizzBuzzManager` class

This class handles the logic for the FizzBuzz problem, managing the tasks for checking divisibility and generating the appropriate output. It uses an `AtomicInteger` to keep track of the current number and a `BlockingQueue` to store the output strings.

#### Methods

- `FizzBuzzManager(int n)`: Constructor to initialize the manager with the maximum number `n`.
- `Runnable createFizzTask()`: Creates a task to handle numbers divisible by 3.
- `Runnable createBuzzTask()`: Creates a task to handle numbers divisible by 5.
- `Runnable createFizzBuzzTask()`: Creates a task to handle numbers divisible by both 3 and 5.
- `Runnable createNumberTask()`: Creates a task to handle numbers not divisible by 3 or 5.
- `int getN()`: Returns the maximum number `n`.
- `AtomicInteger getCurrent()`: Returns the current number.
- `BlockingQueue<String> getOutputQueue()`: Returns the output queue.

### `FizzBuzzExecutor` class

This class executes the FizzBuzz tasks using four threads and collects the results.

#### Methods

- `FizzBuzzExecutor(FizzBuzzManager fizzBuzzManager)`: Constructor to initialize the executor with a FizzBuzzManager.
- `List<String> collectOutput() throws InterruptedException`: Collects and returns the output from the FizzBuzzManager's output queue.
- `void start()`: Starts the execution of FizzBuzz tasks using four threads.

## Usage

Start the FizzBuzz executor and add data to an in-memory collection.

```java
        FizzBuzzManager fizzBuzzManager = new FizzBuzzManager(n);
        FizzBuzzExecutor fizzBuzzExecutor = new FizzBuzzExecutor(fizzBuzzManager);

        fizzBuzzExecutor.start();
        List<String> outputList = fizzBuzzExecutor.collectOutput();
        System.out.println(outputList);

```
### Example Output
For n = 15, the expected output is:

```bash
1, 2, fizz, 4, buzz, fizz, 7, 8, fizz, buzz, 11, fizz, 13, 14, fizzbuzz