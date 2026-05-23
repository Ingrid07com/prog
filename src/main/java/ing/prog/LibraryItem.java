//Det här är grundmallen för allt man kan låna i biblioteket. Eftersom alla grejer (böcker, tidningar, spel) har vissa saker gemensamt
//som ett ID, en titel och en status om de är lediga eller inte så samlar vi det här. 
//Den har också inbyggda spärrar som kollar så att man inte råkar skriva in en tom titel eller ett negativt ID. 
//Dessutom fixar den så att listorna automatiskt kan sorteras i bokstavsordning.

package ing.prog;

/**
 * Abstrakt superklass för samtliga artiklar i biblioteket.
 * Implementerar Comparable för automatisk alfabetisk sortering på titel.
 * * @author Utvecklare
 * @version 3.0 (A-nivå)
 */
public abstract class LibraryItem implements Comparable<LibraryItem> {
    private int id;
    private String title;
    private boolean isAvailable;

    /**
     * Konstruktor som validerar inmatningen direkt vid skapandet.
     * * @param id Unikt artikel-ID
     * @param title Artikelns titel
     * @param isAvailable Tillgänglighetsstatus
     */
    public LibraryItem(int id, String title, boolean isAvailable) {
        setId(id);
        setTitle(title);
        this.isAvailable = isAvailable;
    }

    public int getId() { return id; }
    
    /**
     * Vaktande setter för ID.
     */
    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID måste vara ett positivt heltal över 0.");
        this.id = id;
    }

    public String getTitle() { return title; }
    
    /**
     * Vaktande setter för titel.
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Titeln får inte vara tom.");
        this.title = title.trim();
    }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }

    /**
     * Gör att listor med LibraryItem kan sorteras alfabetiskt (skiftlägesoberoende).
     */
    @Override
    public int compareTo(LibraryItem other) {
        return this.title.compareToIgnoreCase(other.getTitle());
    }
}