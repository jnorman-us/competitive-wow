package ui;

public class BooleanCol {
    public static final String TRUE = "✅";
    public static final String FALSE = "❌";

    public static String from(boolean input) {
        return input ? TRUE : FALSE;
    }
}
