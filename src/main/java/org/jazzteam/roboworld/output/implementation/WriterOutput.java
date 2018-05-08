package org.jazzteam.roboworld.output.implementation;

import org.jazzteam.roboworld.output.Output;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

/**
 * Implementation for write a message through subclasses <code>OutputStream</code>
 */
public class WriterOutput extends Output {
    private static Writer writer;

    /**
     * Sets implementation of <code>OutputStream</code>.
     *
     * @param writer implementation of <code>OutputStream</code>
     * @throws NullPointerException if the specified Writer is <code>null</code>
     */
    public static void setWriter(Writer writer){
        Objects.requireNonNull(writer);
        WriterOutput.writer = writer;
    }

    @Override
    public void write(String message) throws IOException {
        Objects.requireNonNull(writer, "Writer is null");
        writer.write(message);
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
