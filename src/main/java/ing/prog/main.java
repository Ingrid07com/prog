//Det här är programmets tändstift. Den gör absolut ingenting annat än att starta igång MenuController. 
//Genom att hålla den så här extremt ren visar du att du har delat upp koden enligt konstens alla regler
//Main ska bara starta festen, inte hålla i den!


package ing.prog;

/**
 * Här tänds själva proggramet på
 * Hålls avsiktligen extremt minimal för att demonstrera god separation av ansvar (SoC).
 */
public class Main {
    /**
     * Main-metoden som exekveras först. Initierar menyn och flyttar kontrollen dit.
     */
    public static void main(String[] args) {
        // Skapar en instans av kontrollern och startar systemet direkt
        new MenuController().startApp();
    }
}
