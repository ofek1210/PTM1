package test;

import java.io.IOException;
import java.io.OutputStream;
import test.RequestParser.RequestInfo;

@FunctionalInterface
public interface Servlet {
    // מתודה מופשטת אחת בלבד
    void handle(RequestInfo ri, OutputStream toClient) throws IOException;

    // מתודה ברירת מחדל
    default void close() throws IOException {
        // מתודה ריקה כברירת מחדל
    }
}
