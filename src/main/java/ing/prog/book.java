//Den här klassen ärver från LibraryItem. Från början var den bara till för vanliga böcker, 
//men för att slippa skapa en massa nya filer på A-nivån så har vi byggt om den. Nu funkar den som en allt-i-allo-klass 
//som även kan hålla ordning på spel, musik och film. Den har smarta kontroller som ser till att man inte matar in felaktig data 
//(som minusår på en åldersgräns) och den fattar själv hur den ska skriva ut sig på skärmen beroende på om objektet är en bok, en film eller ett spel.

package ing.prog;

/**
 * Representerar en bok, men hanterar nu även spel, musik och film polymorft 
 * för att undvika att skapa nya klasser i systemet.
 */
public class Book extends LibraryItem {
    private String author;     // Används för böcker och som artist för musik
    private String type;       // "book", "game", "music_album" eller "movie"
    private String genre;      // Används av spel och film
    private int age;           // Används av spel
    private int minutes;       // Används av film

    /**
     * Standardkonstruktor för en vanlig bok (C-nivå kompatibel).
     */
    public Book(int id, String title, String author, boolean isAvailable) {
        super(id, title, isAvailable);
        setAuthor(author);
        this.type = "book"; // Standardtyp
    }

    /**
     * Utökad konstruktor för A-nivåns media (spel, musik, film).
     */
    public Book(int id, String title, String author, boolean isAvailable, String type, String genre, int age, int minutes) {
        super(id, title, isAvailable);
        setAuthor(author);
        setType(type);
        setGenre(genre);
        setAge(age);
        setMinutes(minutes);
    }

    // --- Getters och Setters med vaktande villkor ---
    public String getAuthor() { return author; }
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) throw new IllegalArgumentException("Författare/Artist får inte vara tom.");
        this.author = author.trim();
    }

    public String getType() { return type; }
    public void setType(String type) {
        if (type == null || type.trim().isEmpty()) throw new IllegalArgumentException("Typ måste anges.");
        this.type = type.trim();
    }

    public String getGenre() { return genre; }
    public void setGenre(String genre) {
        this.genre = genre != null ? genre.trim() : "";
    }

    public int getAge() { return age; }
    public void setAge(int age) {
        if (age < 0) throw new IllegalArgumentException("Åldersgräns kan inte være negativ.");
        this.age = age;
    }

    public int getMinutes() { return minutes; }
    public void setMinutes(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Minuter kan inte vara negativa.");
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        if ("game".equalsIgnoreCase(type)) {
            return "[Spel] ID: " + getId() + " | " + getTitle() + " (" + genre + "), " + age + "+ år | Tillgänglig: " + isAvailable();
        } else if ("music_album".equalsIgnoreCase(type)) {
            return "[Musik] ID: " + getId() + " | " + getTitle() + " av " + author + " | Tillgänglig: " + isAvailable();
        } else if ("movie".equalsIgnoreCase(type)) {
            return "[Film] ID: " + getId() + " | " + getTitle() + " (" + genre + "), " + minutes + " min | Tillgänglig: " + isAvailable();
        }
        return "[Bok] ID: " + getId() + " | " + getTitle() + " av " + author + " | Tillgänglig: " + isAvailable();
    }
}