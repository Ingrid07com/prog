//Det här är själva motorn och hjärnan i programmet. Den sköter allt grovjobb: pratar med databasen (JSON-servern) för att hämta,
//spara och ta bort grejer, samt styr utlåningen. Den kollar om en kund är spärrad, ändrar status på servern och sparar alla lån i en vanlig textfil
//(active_loans.txt) så att inget försvinner. Det är också här inuti som alla Java Streams bor, vilka används för att filtrera, sortera och räkna i listorna på ett modernt och effektivt sätt.

package ing.prog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.nio.file.*;
import java.net.URI;
import java.net.http.*;
import java.util.*;

public class LibraryManager {
    private final String baseUrl = "http://localhost:3000";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final Path loanFilePath = Paths.get("active_loans.txt");

    // --- HTTP-metoder ---
    private String sendGet(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + endpoint)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private void sendPost(String endpoint, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void sendPut(String endpoint, int id, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void sendDelete(String endpoint, int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + endpoint + "/" + id)).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // --- Hämtningar ---
    public List<Book> getAllBooks() {
        try { return gson.fromJson(sendGet("/books"), new TypeToken<List<Book>>(){}.getType()); } catch (Exception e) { return new ArrayList<>(); }
    }
    public List<Magazine> getAllMagazines() {
        try { return gson.fromJson(sendGet("/magazines"), new TypeToken<List<Magazine>>(){}.getType()); } catch (Exception e) { return new ArrayList<>(); }
    }
    public List<User> getAllUsers() {
        try { return gson.fromJson(sendGet("/users"), new TypeToken<List<User>>(){}.getType()); } catch (Exception e) { return new ArrayList<>(); }
    }
    public List<SuspendedUser> getAllSuspended() {
        try { return gson.fromJson(sendGet("/suspendedUsers"), new TypeToken<List<SuspendedUser>>(){}.getType()); } catch (Exception e) { return new ArrayList<>(); }
    }
    // Hämtar den nya polymorfa listan med Spel, Musik och Film från servern som Book-objekt
    public List<Book> getAllMedia() {
        try { return gson.fromJson(sendGet("/media"), new TypeToken<List<Book>>(){}.getType()); } catch (Exception e) { return new ArrayList<>(); }
    }

    public void createResource(String endpoint, Object item) {
        try {
            sendPost(endpoint, gson.toJson(item));
            IO.println("Sparat framgångsrikt på servern!");
        } catch (Exception e) {
            IO.println("Fel vid lagring: " + e.getMessage());
        }
    }
 
//Filbaserad utlåningslogik direkt i klassen fil  
    public void borrowItem(int userId, int itemId, String type) {
        try {
            // Kontrollera spärr via Stream (.anyMatch)
            boolean isSuspended = getAllSuspended().stream().anyMatch(su -> su.getUserId() == userId);
            if (isSuspended) {
                IO.println("Lån nekas! Kunden är spärrad.");
                return;
            }

            LibraryItem item = null;
            if (type.equalsIgnoreCase("books")) item = getAllBooks().stream().filter(b -> b.getId() == itemId).findFirst().orElse(null);
            else if (type.equalsIgnoreCase("magazines")) item = getAllMagazines().stream().filter(m -> m.getId() == itemId).findFirst().orElse(null);
            else if (type.equalsIgnoreCase("media")) item = getAllMedia().stream().filter(m -> m.getId() == itemId).findFirst().orElse(null);

            if (item == null) { IO.println("Hittades inte."); return; }
            if (!item.isAvailable()) { IO.println("Redan utlånad!"); return; }

            // Uppdatera isAvailable till false på servern
            item.setAvailable(false);
            sendPut("/" + type, itemId, gson.toJson(item));

            // Skriv till filen (append-läget lägger till i slutet av filen)
            String receipt = "Användare " + userId + " lånade ID " + itemId + " (" + item.getTitle() + ") | Datum: " + new Date() + "\n";
            Files.writeString(loanFilePath, receipt, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            IO.println("Utlåning sparad på servern och loggad till 'active_loans.txt'.");
        } catch (Exception e) {
            IO.println("Fel: " + e.getMessage());
        }
    }

    public void returnItem(int itemId, String type) {
        try {
            LibraryItem item = null;
            if (type.equalsIgnoreCase("books")) item = getAllBooks().stream().filter(b -> b.getId() == itemId).findFirst().orElse(null);
            else if (type.equalsIgnoreCase("magazines")) item = getAllMagazines().stream().filter(m -> m.getId() == itemId).findFirst().orElse(null);
            else if (type.equalsIgnoreCase("media")) item = getAllMedia().stream().filter(m -> m.getId() == itemId).findFirst().orElse(null);

            if (item == null) { IO.println("Hittades inte."); return; }

            // Återställer isAvailable till true
            item.setAvailable(true);
            sendPut("/" + type, itemId, gson.toJson(item));
            IO.println("Återlämning slutförd på servern!");
        } catch (Exception e) {
            IO.println("Fel: " + e.getMessage());
        }
    }

    public void printActiveLoansFromFile() {
        try {
            if (!Files.exists(loanFilePath)) { IO.println("Inga lån registrerade på fil."); return; }
            IO.println("\n--- INLÄST HISTORIK FRÅN FIL ---");
            Files.readAllLines(loanFilePath).forEach(IO::println);
        } catch (Exception e) { IO.println("Kunde inte läsa fil."); }
    }

    
    //java streams metoder = är som ett löpande band. 
    //Det är en kedja av instruktioner som bearbetar din data i farten,
    //så att du slipper skriva långa for-loopar och if-satser.

    public void streamFilterMediaByGenre(String genre) {
        IO.println("\n---Filtrerat på genre: [" + genre + "] (.filter) ---");
        getAllMedia().stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre.trim()))
                .forEach(IO::println);
    }

    public void streamSortMediaByIdDescending() {
        IO.println("\n--- All media sorterad fallande på ID (.sorted) ---");
        getAllMedia().stream()
                .sorted((m1, m2) -> Integer.compare(m2.getId(), m1.getId()))
                .forEach(IO::println);
    }

    public void streamCountBooksByAuthor(String author) {
        long count = getAllBooks().stream()
                .filter(b -> b.getAuthor().equalsIgnoreCase(author.trim()))
                .count();
        IO.println("Författaren '" + author + "' har skrivit " + count + " bok/böcker (.count).");
    }

    public void streamMapToUppercaseTitles() {
        IO.println("\n--- Medietitlar transformerade till VERSALER (.map) ---");
        getAllMedia().stream()
                .map(m -> m.getTitle().toUpperCase())
                .forEach(IO::println);
    }

    // --- Tidigare logik ---
    public void deleteMediaByTitle(String title) {
        try {
            Optional<Book> b = getAllBooks().stream().filter(x -> x.getTitle().equalsIgnoreCase(title)).findFirst();
            if (b.isPresent()) { sendDelete("/books", b.get().getId()); IO.println("Bok raderad."); return; }
            Optional<Book> m = getAllMedia().stream().filter(x -> x.getTitle().equalsIgnoreCase(title)).findFirst();
            if (m.isPresent()) { sendDelete("/media", m.get().getId()); IO.println("Media raderad."); return; }
            IO.println("Hittades ej.");
        } catch (Exception e) { IO.println("Fel."); }
    }

    public void checkLoanStatus(String email) {
        Optional<User> u = getAllUsers().stream().filter(x -> x.getEmail().equalsIgnoreCase(email)).findFirst();
        if (u.isEmpty()) { IO.println("Hittades ej."); return; }
        boolean susp = getAllSuspended().stream().anyMatch(s -> s.getUserId() == u.get().getId());
        IO.println(susp ? "SPÄRRAD" : "✔ OK");
    }

    public void printSortedLists() {
        List<Book> books = getAllBooks();
        List<Magazine> magazines = getAllMagazines();
        Collections.sort(books);
        Collections.sort(magazines);
        IO.println("\n--- BÖCKER ---"); books.forEach(IO::println);
        IO.println("\n--- TIDNINGAR ---"); magazines.forEach(IO::println);
    }
}