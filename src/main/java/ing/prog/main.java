package ing.prog;

/**
 * Startklass för bibliotekssystemet LibSys.
 * * @author Utvecklare
 * @version 3.0 (A-nivå)
 */
public class Main {
    /**
     * Huvudmetod som startar programmet.
     * @param args Programargument (används ej)
     */
    public static void main(String[] args) {
        MenuController ui = new MenuController();
        ui.start();
    }
}