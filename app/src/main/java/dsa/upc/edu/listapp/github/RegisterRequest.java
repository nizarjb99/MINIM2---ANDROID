package dsa.upc.edu.listapp.github;

public class RegisterRequest {
    public final String username;
    public final String name;
    public final String email;
    public final String password;

    public RegisterRequest(String username, String name,
                           String email, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
