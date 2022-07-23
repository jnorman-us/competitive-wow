package types;

public class User {
    private String username;
    private String password;

    public User(String u, String p) {
        username = u;
        password = p;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return username;
    }
}
