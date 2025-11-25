package dsa.upc.edu.listapp.github;

public class User {
    public final int id;
    public final String username;
    public final String nom;
    public final String email;
    public final String password;

    public User(int id, String username, String nom, String email, String password) {
        this.id = id;
        this.username = username;
        this.nom = nom;
        this.email = email;
        this.password = password;
    }
}
