package ing.prog;


public class SuspendedUser {
    private int id;
    private int userId;

    public SuspendedUser(int id, int userId) {
        if (id <= 0 || userId <= 0) throw new IllegalArgumentException("ID-värden måste vara positiva.");
        this.id = id;
        this.userId = userId;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }

    @Override
    public String toString() {
        return "[Spärrad] Spärr-ID: " + id + " | Kund-ID: " + userId;
    }
}