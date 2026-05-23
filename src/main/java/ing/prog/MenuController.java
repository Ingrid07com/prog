//Det här är gränssnittet som användaren ser och klickar runt i. Den snurrar i en loop och visar menyerna,
//tar emot det man skriver på tangentbordet, städar upp inmatningen och säger till motorn (LibraryManager) vad den ska utföra.


package ing.prog;

public class MenuController {
    private final LibraryManager manager = new LibraryManager();

    public void start() {
        while (true) {
            IO.println("\n================= SYSTEMMENY =================");
            IO.println("1. Visa alla register (inkl. Ny Media)");
            IO.println("2. Registrera ny resurs (Bok / Media)");
            IO.println("3. Hantera Lån och Återlämning (Filbaserad)");
            IO.println("4. Kontrollera kundstatus");
            IO.println("5. Kör A-nivåns Streams-operationer");
            IO.println("6. Ta bort media via titel");
            IO.println("9. Avsluta");
            IO.println("==============================================");
            IO.print("Val: ");

            switch (IO.readln().trim()) {
                case "1" -> {
                    IO.println("\n--- BÖCKER ---"); manager.getAllBooks().forEach(IO::println);
                    IO.println("\n--- TIDNINGAR ---"); manager.getAllMagazines().forEach(IO::println);
                    IO.println("\n--- ARVSHIERARKI FÖR MEDIA (SPEL/MUSIK/FILM) ---"); manager.getAllMedia().forEach(IO::println);
                }
                case "2" -> showCreateMenu();
                case "3" -> showLoanMenu();
                case "4" -> { IO.print("Ange kundens e-post: "); manager.checkLoanStatus(IO.readln()); }
                case "5" -> showStreamsMenu();
                case "6" -> { IO.print("Titel: "); manager.deleteMediaByTitle(IO.readln()); }
                case "9" -> System.exit(0);
            }
        }
    }

    private void showCreateMenu() {
        IO.println("\n1. Lägg till vanlig Bok\n2. Lägg till Media (Spel/Musik/Film)");
        IO.print("Val: ");
        String val = IO.readln();

        IO.print("Ange unikt ID: "); int id = IO.readInt();
        IO.print("Ange titel: "); String title = IO.readln();

        if (val.equals("1")) {
            IO.print("Ange författare: "); String author = IO.readln();
            manager.createResource("/books", new Book(id, title, author, true));
        } else {
            IO.print("Ange typ (game/music_album/movie): "); String type = IO.readln().trim();
            if (type.equalsIgnoreCase("game")) {
                IO.print("Genre: "); String genre = IO.readln();
                IO.print("Åldersgräns: "); int age = IO.readInt();
                manager.createResource("/media", new Book(id, title, "N/A", true, "game", genre, age, 0));
            } else if (type.equalsIgnoreCase("music_album")) {
                IO.print("Artist: "); String artist = IO.readln();
                manager.createResource("/media", new Book(id, title, artist, true, "music_album", "N/A", 0, 0));
            } else if (type.equalsIgnoreCase("movie")) {
                IO.print("Genre: "); String genre = IO.readln();
                IO.print("Längd i minuter: "); int min = IO.readInt();
                manager.createResource("/media", new Book(id, title, "N/A", true, "movie", genre, 0, min));
            }
        }
    }

    private void showLoanMenu() {
        IO.println("\n1. Registrera nytt lån (isAvailable -> false)\n2. Returnera artikel (isAvailable -> true)\n3. Läs in och visa lånehistorik från fil");
        IO.print("Val: ");
        String val = IO.readln();

        if (val.equals("3")) {
            manager.printActiveLoansFromFile();
            return;
        }

        IO.print("Ange artikel-ID: "); int itemId = IO.readInt();
        IO.print("Ange registertyp (books/magazines/media): "); String type = IO.readln().trim().toLowerCase();

        if (val.equals("1")) {
            IO.print("Ange Kundens ID: "); int uid = IO.readInt();
            manager.borrowItem(uid, itemId, type);
        } else if (val.equals("2")) {
            manager.returnItem(itemId, type);
        }
    }

    private void showStreamsMenu() {
        IO.println("\n--- REKVISIT: STREAMS ---");
        IO.println("1. Visa endast media av viss genre (.filter)");
        IO.println("2. Sortera media fallande på ID (.sorted)");
        IO.println("3. Visa hur många böcker en författare har gjort (.count)");
        IO.println("4. Mappa medietitlar till VERSALER (.map)");
        IO.print("Val: ");

        switch (IO.readln().trim()) {
            case "1" -> { IO.print("Ange genre: "); manager.streamFilterMediaByGenre(IO.readln()); }
            case "2" -> manager.streamSortMediaByIdDescending();
            case "3" -> { IO.print("Ange författare: "); manager.streamCountBooksByAuthor(IO.readln()); }
            case "4" -> manager.streamMapToUppercaseTitles();
        }
    }
}