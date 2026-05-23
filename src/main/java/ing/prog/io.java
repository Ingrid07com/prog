//En liten smidig verktygsklass som fungerar som en mellanhand mellan tangentbordet/skärmen och resten av programmet. Istället för att skriva 
//Javas långa standardkommandon överallt, använder de andra klasserna (framförallt MenuController) 
//den här klassen för att skriva ut text och läsa in vad användaren svarar. 
//Den har också ett inbyggt skydd så att programmet inte kraschar om man råkar skriva bokstäver när programmet vill ha en siffra.

package ing.prog;

import java.util.Scanner;

/**
 * Hjälpklass för att kapsla in hanteringen av konsolinmatning och utskrifter.
 * Skyddar systemet från krascher orsakade av felaktiga datatyper i konsolen.
 */
public class IO {

    // Skapar en privat, konstant skanner som läser från tangentbordet (System.in).
    // 'static' gör att alla metoder i klassen delar på samma skanner instans.
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Genväg för System.out.println(). Skriver ut text och gör ett radbyte.
     * Tar emot ett 'Object', vilket gör att du kan skicka in både text, siffror
     * eller hela objekt.
     */
    public static void println(Object msg) {
        System.out.println(msg);
    }

    /**
     * Genväg för System.out.print(). Skriver ut text UTAN att göra ett radbyte.
     * Perfekt för ledtexter i menyn där markören ska stanna kvar på samma rad.
     */
    public static void print(Object msg) {
        System.out.print(msg);
    }

    /**
     * Pausar programmet och läser in nästa rad som användaren skriver på
     * tangentbordet.
     * 
     * @return Den inskrivna texten som en String så fort användaren trycker på
     *         Enter.
     */
    public static String readln() {
        return scanner.nextLine();
    }

    public static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ange ett heltal: ");
            }
        }
    }
}
