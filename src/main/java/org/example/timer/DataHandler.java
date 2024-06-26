package org.example.timer;

/**
 * The {@code DataHandler} interface provides methods for writing and reading data.
 */
public interface DataHandler {
    /**
     * Writes the specified data.
     *
     * @param data the data to be written
     */
    void writeData(String data);

    /**
     * Reads the data.
     */
    void readData();
}
