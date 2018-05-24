package by.roboworld.output;

import java.io.Closeable;
import java.io.IOException;

/**
 * This super class for any information output class.
 */
public abstract class Output implements Closeable {

    /**
     * Writes the message.
     *
     * @param message the message
     * @throws IOException most often occurs when the output class has been closed
     */
    public abstract void write(String message) throws IOException;

}
