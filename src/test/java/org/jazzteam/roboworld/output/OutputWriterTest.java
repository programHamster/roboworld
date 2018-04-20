package org.jazzteam.roboworld.output;

import org.junit.Test;

public class OutputWriterTest {

    @Test(expected = NullPointerException.class)
    public void installOutput_null() throws InstantiationException{
        OutputWriter.installOutput(null);
    }
}