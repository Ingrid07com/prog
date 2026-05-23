//Den här ärver också från LibraryItem och håller koll på tidningar. Den är väldigt simpel och kort eftersom den får alla sina funktioner
//och säkerhetskontroller gratis från grundmallen, men den har en egen liten textsnutt för hur en tidning ska visas snyggt i listorna.


package ing.prog;

/**
 * Representerar en tidskrift eller tidning i biblioteket.
 * * @author Utvecklare
 * @version 
 */
public class Magazine extends LibraryItem {

    /**
     * Skapar en ny tidskrift. Vaktas via superklassens konstruktor.
     */
    public Magazine(int id, String title, boolean isAvailable) {
        super(id, title, isAvailable);
    }

    @Override
    public String toString() {
        return "[Tidning] ID: " + getId() + " | Titel: " + getTitle() + " | Tillgänglig: " + isAvailable();
    }
}