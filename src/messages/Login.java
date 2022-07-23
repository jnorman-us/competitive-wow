package messages;

import types.User;

public class Login extends Message {
    public static final String TYPE = "LOGIN";

    public String username;
    public String password;

    public Login(String username, String password) {
        super(TYPE);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
