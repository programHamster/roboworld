package by.roboworld.output;

import by.roboworld.Constants;
import by.roboworld.output.implementation.StreamOutput;
import by.roboworld.output.implementation.WriterOutput;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Objects;

/**
 * This class is used to display information about what is happening in the
 * game world. The default way to display information is the console
 * ({@code System.out.println()}. For change the way information is displayed,
 * you must implement it in method {@code write(String message)} and associate
 * it with a specific name.
 */
public abstract class OutputInformation {
    /** The default way to display information. */
    private static Output output = new StreamOutput();

    /**
     * Installs the way information is displayed.
     *
     * @param output an output
     * @throws NullPointerException if the specified output is <code>null</code>
     */
    public static void installOutput(final Output output) {
        Objects.requireNonNull(output, by.roboworld.exception.Constants.OUTPUT_IS_NULL);
        OutputInformation.output = output;
    }

    /**
     * Sets a new PrintStream for output information.
     *
     * @param printStream a PrintStream
     */
    public static void setOutputStream(final OutputStream printStream) {
        Objects.requireNonNull(printStream);
        StreamOutput.setOutputStream(printStream);
    }

    /**
     * Sets a new Writer for output information.
     *
     * @param writer a Writer
     */
    public static void setWriter(final Writer writer) {
        Objects.requireNonNull(writer);
        WriterOutput.setWriter(writer);
    }

    /**
     * Sends the message to the installed output.
     *
     * @param message message to output
     * @throws UncheckedIOException if the output was closed
     */
    public static void write(final String message) {
        try {
            output.write(message);
        } catch (IOException e) {
            throw new UncheckedIOException(e.getMessage(), e);
        }
    }

    /**
     * Adds the specified event key to the message and sends it to the output.
     * If the status or number of robots or tasks changed, necessary specify
     * the appropriate event to trigger data refresh on the client side.
     *
     * @param message message to output
     * @param event event occurred in the game world
     * @throws NullPointerException if the specified event is <code>null</code>
     * @throws IllegalArgumentException if the output name is not associated
     *                                  with the output way
     */
    public static void write(final String message, final RoboworldEvent event) {
        Objects.requireNonNull(event, by.roboworld.exception.Constants.EVENT_IS_NULL);
        String newMessage = event.name().toLowerCase() + Constants.KEY_DELIMITER + message;
        write(newMessage);
    }

}
