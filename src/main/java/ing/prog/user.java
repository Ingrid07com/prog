package ing.prog;


public class User {
    private int id;
    private String name;
    private String email;

    public User(int id, String name, String email) {
        setId(id);
        setName(name);
        setEmail(email);
    }

    public int getId() { return id; }
    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("Kund-ID måste vara över 0.");
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Namn kan inte vara tomt.");
        this.name = name.trim();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email == null || !email.contains("@")) throw new IllegalArgumentException("Ogiltig e-postadress.");
        this.email = email.trim();
    }

    @Override
    public String toString() {
        return "[Kund] ID: " + id + " | Namn: " + name + " | E-post: " + email;
    }
}