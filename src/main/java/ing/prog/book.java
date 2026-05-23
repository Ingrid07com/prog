package ing.prog.model;

/**
 * Representerar en bok och ärver gemensamma datafält från LibraryItem.
 */
public class Book extends LibraryItem {
    private String author;

    public Book(String id, String title, String author) {
        super(id, title); // Anropar superklassens konstruktor
        this.author = author;
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    @Override
    public String toString() {
        return "[Bok] ID: " + getId() + " | Titel: " + getTitle() + " | Författare: " + author;
    }
}