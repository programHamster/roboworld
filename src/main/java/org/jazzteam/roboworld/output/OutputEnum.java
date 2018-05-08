package org.jazzteam.roboworld.output;

import org.jazzteam.roboworld.output.implementation.StreamOutput;
import org.jazzteam.roboworld.output.implementation.WebSocketOutput;
import org.jazzteam.roboworld.output.implementation.WriterOutput;

import java.io.IOException;

public enum OutputEnum {
    WEB_SOCKET{{
        output = new WebSocketOutput();
    }},
    OUTPUT_STREAM{{
        output = new StreamOutput();
    }},
    WRITER{{
        output = new WriterOutput();
    }};
    protected Output output;

    public void write(String message) throws IOException {
        output.write(message);
    }
}
