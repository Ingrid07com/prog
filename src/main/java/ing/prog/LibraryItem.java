//Det här är en abstrakt superklass, vilket betyder att det är en mall som man inte kan skapa direkt, utan andra klasser ärver ifrån den. 
// Den håller koll på sånt som all media i biblioteket har gemensamt: ett ID och en titel. 
// Den ser också till att allt som ärver från den automatiskt går att sortera i bokstavsordning på titeln. 
package ing.prog;

/**
 * Abstrakt superklass för alla typer av media i biblioteket.
 * Innehåller gemensamma attribut och hanterar alfabetisk sortering på titel.
 */
public abstract class LibraryItem implements Comparable<LibraryItem> {
    private String id;
    private String title;

    public LibraryItem(String id, String title) {
        setId(id);
        setTitle(title);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    /**
     * Gör det möjligt att sortera alla typer av media (böcker och tidningar)
     * baserat på deras titel.
     */
    @Override
    public int compareTo(LibraryItem other) {
        return this.title.compareToIgnoreCase(other.getTitle());
    }
}