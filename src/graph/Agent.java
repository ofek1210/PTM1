package graph;

public interface Agent {
    String getName();
    void callback(String topic, Message msg);
    void close();
    void reset();
}
