package ing.prog;

/**
 * Representerar en avstängningspost (spärr) för en kund som brutit mot reglerna.
 * Den kopplar ihop ett specifikt spärr-ID med kundens unika användar-ID.
 * * @author Utvecklare
 * @version 1.0
 */
public class SuspendedUser {
    // Privata variabler för att skydda datatillståndet
    private String id;
    private String userId;

    /**
     * Skapar en ny avstängningspost.
     * * @param id Det unika ID-numret för själva spärren.
     * @param userId ID-numret på den kund som ska blockeras.
     */
    public SuspendedUser(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    // Getters och Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    /**
     * Returnerar en textrepresentation av spärren för administrationen.
     */
    @Override
    public String toString() {
        return "[Spärrad] Spärr-ID: " + id + " | Tillhör Användar-ID: " + userId;
    }
}