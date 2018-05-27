package by.roboworld.output.implementation;

import by.roboworld.output.Output;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Implementation for write a message through subclasses
 * <code>OutputStream</code>.
 */
public class StreamOutput extends Output {
    /** The default output stream. */
    private static OutputStream outputStream = System.out;

    /**
     * Sets implementation of <code>OutputStream</code>.
     *
     * @param outputStream implementation of <code>OutputStream</code>
     * @throws NullPointerException if the specified outputStream is
     *                              <code>null</code>
     */
    public static void setOutputStream(OutputStream outputStream) {
        Objects.requireNonNull(outputStream);
        StreamOutput.outputStream = outputStream;
    }

    @Override
    public void write(String message) throws IOException {
        outputStream.write(message.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

}
