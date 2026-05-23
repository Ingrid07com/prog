//Det är här hela användarupplevelsen bor. Den sköter logiken för huvudmenyn och undermenyerna (som skapande och kundmenyn). 
// Den använder IO för att prata med användaren i terminalen, tar emot valen, och ropar sedan på LibraryManager
// för att faktiskt utföra sakerna på servern (t.ex. "Spara den här boken" eller "Hämta den här kunden").


package ing.prog;

/**
 * Kontrollerklass ansvarig för att rendera menyer samt tolka användarens val.
 * Håller huvudprogrammet fritt från användargränssnittslogik.
 */
public class MenuController {
    private final LibraryManager manager = new LibraryManager();

    /**
     * Startar och driver applikationens kommandoloop.
     * Omgärdas av en try-catch för att förhindra oväntade systemkrascher vid felinmatning.
     */
    public void startApp() {
        IO.println("Välkommen");
        while (true) {
            try {
                showMainMenu();
            } catch (Exception e) {
                IO.println("Ett inmatningsfel inträffade. Återställer menyn...");
            }
        }
    }

    /**
     * Visar huvudmenyn och skickar vidare användaren till rätt underfunktion.
     */
    private void showMainMenu() {
        IO.println("\n================= HUVUDMENY =================");
        IO.println("1. Visa alla resurser på servern");
        IO.println("2. Skapa ny resurs... (Undermeny)");
        IO.println("3. Hitta kund via e-post");
        IO.println("4. Hitta bok/tidning via titel");
        IO.println("5. Ta bort media via titel");
        IO.println("6. Kundadministration... (Undermeny)");
        IO.println("7. Skriv ut listor sorterade (Comparable)");
        IO.println("8. Kontrollera lånerätt");
        IO.println("9. Avsluta");
        IO.println("=============================================");
        IO.print("Val: ");

        switch (IO.readln().trim()) {
            case "1" -> {
                IO.println("\n--- BÖCKER ---"); manager.getAllBooks().forEach(IO::println);
                IO.println("\n--- TIDNINGAR ---"); manager.getAllMagazines().forEach(IO::println);
                IO.println("\n--- KUNDER ---"); manager.getAllUsers().forEach(IO::println);
                IO.println("\n--- SPÄRRADE KUNDER ---"); manager.getAllSuspended().forEach(IO::println);
            }
            case "2" -> showCreateSubMenu();
            case "3" -> { IO.print("E-post: "); manager.findUserByEmail(IO.readln()); }
            case "4" -> { IO.print("Titel: "); manager.findMediaByTitle(IO.readln()); }
            case "5" -> { IO.print("Titel att radera: "); manager.deleteMediaByTitle(IO.readln()); }
            case "6" -> showCustomerSubMenu();
            case "7" -> manager.printSortedLists();
            case "8" -> { IO.print("Kundens e-post: "); manager.checkLoanStatus(IO.readln()); }
            case "9" -> { IO.println("Avslutar..."); System.exit(0); }
            default -> IO.println("Ogiltigt val.");
        }
    }

    /**
     * Undermeny för att skapa och registrera nya datatransaktioner.
     */
    private void showCreateSubMenu() {
        IO.println("\n--- SKAPA NY RESURS ---");
        IO.println("1. Ny Bok");
        IO.println("2. Ny Tidning");
        IO.println("3. Ny Användare");
        IO.println("4. Gå tillbaka");
        IO.print("Val: ");

        switch (IO.readln().trim()) {
            case "1" -> {
                IO.print("ID: "); String id = IO.readln();
                IO.print("Titel: "); String title = IO.readln();
                IO.print("Författare: "); String author = IO.readln();
                manager.createResource("/books", new Book(id, title, author));
            }
            case "2" -> {
                IO.print("ID: "); String id = IO.readln();
                IO.print("Titel: "); String title = IO.readln();
                IO.print("Utgåvonummer: "); int issue = IO.readInt();
                manager.createResource("/magazines", new Magazine(id, title, issue));
            }
            case "3" -> {
                IO.print("ID: "); String id = IO.readln();
                IO.print("Fullständigt Namn: "); String name = IO.readln();
                IO.print("E-post: "); String email = IO.readln();
                manager.createResource("/users", new User(id, name, email));
            }
        }
    }

    /**
     * Undermeny dedikerad till administration av kunder och avstängningar.
     */
    private void showCustomerSubMenu() {
        IO.println("\n--- KUNDADMINISTRATION ---");
        IO.println("1. Ta bort kund via e-post");
        IO.println("2. Spärra kund");
        IO.println("3. Häv spärr via spärr-ID");
        IO.println("4. Gå tillbaka");
        IO.print("Val: ");

        switch (IO.readln().trim()) {
            case "1" -> { IO.print("Kundens e-post: "); manager.deleteUserByEmail(IO.readln()); }
            case "2" -> {
                IO.print("Spärr-ID (t.ex. 1): "); String id = IO.readln();
                IO.print("Användar-ID på den som ska spärras: "); String uid = IO.readln();
                manager.createResource("/suspendedUsers", new SuspendedUser(id, uid));
            }
            case "3" -> { IO.print("Spärr-ID att radera: "); manager.deleteSuspensionById(IO.readln()); }
        }
    }
}