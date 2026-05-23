//Den här klassen beskriver en bibliotekskund med ID, namn och e-post. 
//Den är helt fristående från mediatyperna, men den är superviktig för SuspendedUser. 
//När du sorterar dina kunder i programmet använder den här klassen en inbyggd regel för att sortera alla namn 
//i alfabetisk ordning (A–Ö).


package ing.prog;

/**
 * Representerar en registrerad bibliotekskund i systemet.
 * Klassen implementerar {@link Comparable} för att möjliggöra automatisk
 * sortering av kunder baserat på deras namn i bokstavsordning.
 * * @author Utvecklare
 * @version 1.0
 */
public class User implements Comparable<User> {
    // Inkapslade instansvariabler (private för att förhindra direkt extern manipulation)
    private String id;
    private String name;
    private String email;

    /**
     * Konstruerar en ny användare och sätter initiala värden via dess setters.
     * * @param id Kundens unika ID-nummer på servern.
     * @param name Kundens för- och efternamn.
     * @param email Kundens unika e-postadress.
     */
    public User(String id, String name, String email) {
        setId(id);
        setName(name);
        setEmail(email);
    }

    // Standard Getters och Setters för fullständig inkapsling
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    /**
     * Jämför den här kunden med en annan kund baserat på namn.
     * Används av Collections.sort() för att sortera listor skiftlägesokänsligt (A-Ö).
     * * @param other Den andra användaren som den här ska jämföras med.
     * @return Ett negativt heltal, noll, eller ett positivt heltal.
     */
    @Override
    public int compareTo(User other) {
        return this.name.compareToIgnoreCase(other.getName());
    }

    /**
     * Returnerar en snyggt formaterad textsträng av kunden för utskrift i terminalen.
     */
    @Override
    public String toString() {
        return "[Kund] ID: " + id + " | Namn: " + name + " | E-post: " + email;
    }
}