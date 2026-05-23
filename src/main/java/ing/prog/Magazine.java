//Den här klassen gör nästan exakt samma sak som bok-klassen, men är helt anpassad för tidningar. 
//Den ärver också ID och titel från LibraryItem, men istället för en författare har den ett eget unika fält för utgåvonummer 
//(sparat som ett heltal, int). Den fungerar som en helt egen, separat datatyp i programmet så att LibraryManager och menyn kan hålla isär tidningar från vanliga böcker, 
//vilket gör det smidigare för användaren att söka på rätt typ av media.

package ing.prog;

/**
 * Representerar en tidning och utökar LibraryItem med specifika utgåvonummer.
 */
public class Magazine extends LibraryItem {
    private int issueNumber;

    public Magazine(String id, String title, int issueNumber) {
        super(id, title); // Anropar superklassens konstruktor
        this.issueNumber = issueNumber;
    }

    public int getIssueNumber() { return issueNumber; }
    public void setIssueNumber(int issueNumber) { this.issueNumber = issueNumber; }

    @Override
    public String toString() {
        return "[Tidning] ID: " + getId() + " | Titel: " + getTitle() + " | Nummer: " + issueNumber;
    }
}