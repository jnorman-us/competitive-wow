package messages;

public class Message implements java.io.Serializable {
    public String type;

    protected Message(String t) {
        type = t;
    }
}
