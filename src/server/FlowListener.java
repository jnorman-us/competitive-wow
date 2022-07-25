package server;

public interface FlowListener {
    void roundStart();
    void roundFinish();

    void matchStart();
    void matchFinish();
}
