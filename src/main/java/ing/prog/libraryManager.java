package ing.prog.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ing.prog.model.*;
import ing.prog.ui.IO;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.util.*;

/**
 * Central motor för systemet. Sköter all HTTP-kommunikation (REST API)
 * mot JSON-servern samt hanterar logik för filtrering, sökning och spärrkontroller.
 */
public class LibraryManager {
    private final String baseUrl = "http://localhost:3000";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    /**
     * Skickar ett synkront HTTP GET-anrop till angiven slutpunkt.
     * * @param endpoint Resursens sökväg (t.ex. "/users").
     * @return Serverns svar i rått JSON-format som en sträng.
     */
    private String sendGet(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + endpoint)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    /**
     * Skickar ett HTTP POST-anrop med JSON-data för att registrera ett nytt objekt på servern.
     * * @param endpoint Sökväg där objektet ska sparas.
     * @param json Den serialiserade strängen som representerar objektet.
     */
    private void sendPost(String endpoint, String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Skickar ett HTTP DELETE-anrop för att radera en resurs via dess specifika ID.
     * * @param endpoint Slutpunkten för resursen.
     * @param id Databas-ID:t på posten som ska tas bort.
     */
    private void sendDelete(String endpoint, String id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(baseUrl + endpoint + "/" + id)).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // --- GET-METODER FÖR ATT HÄMTA SAMLINGAR FRÅN SERVER ---

    public List<Book> getAllBooks() {
        try { return gson.fromJson(sendGet("/books"), new TypeToken<List<Book>>(){}.getType()); } 
        catch (Exception e) { return new ArrayList<>(); }
    }

    public List<Magazine> getAllMagazines() {
        try { return gson.fromJson(sendGet("/magazines"), new TypeToken<List<Magazine>>(){}.getType()); } 
        catch (Exception e) { return new ArrayList<>(); }
    }

    public List<User> getAllUsers() {
        try { return gson.fromJson(sendGet("/users"), new TypeToken<List<User>>(){}.getType()); } 
        catch (Exception e) { return new ArrayList<>(); }
    }

    public List<SuspendedUser> getAllSuspended() {
        try { return gson.fromJson(sendGet("/suspendedUsers"), new TypeToken<List<SuspendedUser>>(){}.getType()); } 
        catch (Exception e) { return new ArrayList<>(); }
    }

    /**
     * Konverterar ett Java-objekt till JSON och laddar upp det till servern.
     * * @param endpoint Målmappen på servern (t.ex. "/books").
     * @param item Objektet som ska serialiseras och skickas.
     */
    public void createResource(String endpoint, Object item) {
        try {
            sendPost(endpoint, gson.toJson(item));
            IO.println("✔ Sparat framgångsrikt på servern!");
        } catch (Exception e) {
            IO.println("Fel vid lagring: " + e.getMessage());
        }
    }

    /**
     * Söker igenom listan av användare efter en specifik e-postadress.
     * * @param email E-postadressen som ska matchas (skiftlägesokänsligt).
     */
    public void findUserByEmail(String email) {
        for (User user : getAllUsers()) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                IO.println("Träff: " + user);
                return;
            }
        }
        IO.println("Ingen kund hittades med e-post: " + email);
    }

    /**
     * Söker efter media (både böcker och tidningar) polymorft via dess titel.
     * * @param title Söksträngen (titeln) som användaren letar efter.
     */
    public void findMediaByTitle(String title) {
        List<LibraryItem> combinedMedia = new ArrayList<>();
        combinedMedia.addAll(getAllBooks());
        combinedMedia.addAll(getAllMagazines());

        boolean found = false;
        for (LibraryItem item : combinedMedia) {
            if (item.getTitle().equalsIgnoreCase(title.trim())) {
                IO.println("Träff: " + item);
                found = true;
            }
        }
        if (!found) IO.println("Inget medie hittades med titeln: " + title);
    }

    /**
     * Letar upp en bok eller tidning baserat på dess titel, identifierar dess ID,
     * och skickar sedan en begäran om att radera den från servern.
     * * @param title Titeln på det medie som ska tas bort.
     */
    public void deleteMediaByTitle(String title) {
        try {
            // Sök i böcker
            for (Book b : getAllBooks()) {
                if (b.getTitle().equalsIgnoreCase(title)) {
                    sendDelete("/books", b.getId());
                    IO.println("✔ Boken '" + title + "' raderad från servern.");
                    return;
                }
            }
            // Sök i tidningar
            for (Magazine m : getAllMagazines()) {
                if (m.getTitle().equalsIgnoreCase(title)) {
                    sendDelete("/magazines", m.getId());
                    IO.println("✔ Tidningen '" + title + "' raderad från servern.");
                    return;
                }
            }
            IO.println("Mediet hittades inte.");
        } catch (Exception e) {
            IO.println("Fel vid borttagning: " + e.getMessage());
        }
    }

    /**
     * Letar upp en kund via dennes e-postadress och raderar kontot från servern.
     * * @param email E-postadressen tillhörande kunden som ska tas bort.
     */
    public void deleteUserByEmail(String email) {
        try {
            for (User u : getAllUsers()) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    sendDelete("/users", u.getId());
                    IO.println("✔ Kunden med e-post " + email + " borttagen.");
                    return;
                }
            }
            IO.println("Hittade ingen kund med den e-postadressen.");
        } catch (Exception e) {
            IO.println("Fel: " + e.getMessage());
        }
    }

    /**
     * Tar bort en spärrpost från servern vilket ger kunden tillbaka sina lånerättigheter.
     * * @param id Det unika spärr-ID:t (inte kundens användar-ID).
     */
    public void deleteSuspensionById(String id) {
        try {
            sendDelete("/suspendedUsers", id);
            IO.println("✔ Spärren har hävts på servern.");
        } catch (Exception e) {
            IO.println("Misslyckades att häva spärr: " + e.getMessage());
        }
    }

    /**
     * Kontrollerar om en specifik kund har rätt att låna genom att verifiera
     * om kundens unika ID finns med i listan över spärrade användare.
     * * @param email E-postadressen till kunden som ska kontrolleras.
     */
    public void checkLoanStatus(String email) {
        User targetUser = null;
        // Hitta först användaren via e-post
        for (User u : getAllUsers()) {
            if (u.getEmail().equalsIgnoreCase(email.trim())) {
                targetUser = u;
                break;
            }
        }

        if (targetUser == null) {
            IO.println("Kunden hittades inte.");
            return;
        }

        // Kontrollera om användarens ID matchar ett ID i spärrlistan
        boolean isSuspended = false;
        for (SuspendedUser su : getAllSuspended()) {
            if (su.getUserId().equals(targetUser.getId())) {
                isSuspended = true;
                break;
            }
        }

        // Ge feedback baserat på sökresultatet
        if (isSuspended) {
            IO.println("LÅNEFÖRBUD: Kunden '" + targetUser.getName() + "' är för närvarande avstängd!");
        } else {
            IO.println("STATUS OK: Kunden '" + targetUser.getName() + "' har fullständiga lånerättigheter.");
        }
    }

    /**
     * Hämtar rådata från databasen, sorterar alla samlingar i minnet med hjälp av
     * de regler som definierats i klassernas `compareTo`-metoder, och skriver ut dem.
     */
    public void printSortedLists() {
        List<Book> books = getAllBooks();
        List<Magazine> magazines = getAllMagazines();
        List<User> users = getAllUsers();

        // Sorterar objekten i listorna (kräver att klasserna implementerar Comparable)
        Collections.sort(books);
        Collections.sort(magazines);
        Collections.sort(users);

        // Skriv ut resultatet
        IO.println("\n--- BÖCKER (Sorterade på Titel) ---");
        books.forEach(IO::println);

        IO.println("\n--- TIDNINGAR (Sorterade på Titel) ---");
        magazines.forEach(IO::println);

        IO.println("\n--- KUNDER (Sorterade på Namn) ---");
        users.forEach(IO::println);
    }
}