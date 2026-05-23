//Den här klassen är en ritning för hur en bok ska se ut i programmet. 
//Eftersom den ärver från LibraryItem får den automatiskt med sig grundläggande saker som ID och titel. 
//Det enda den här klassen faktiskt lägger till på egen hand är ett textfält för författare. 
//Den används av LibraryManager varje gång systemet ska hämta, spara eller visa böcker från servern, 
//och den vet själv hur den ska formatera sina uppgifter till en snygg textrad när den skrivs ut i en lista.


package ing.prog;

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