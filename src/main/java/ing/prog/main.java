package ing.prog;

/**
 * Startpunkt för applikationen.
 * Hålls minimal genom att överlåta allt till MenuController.
 */
public class Main {
    public static void main(String[] args) {
        new MenuController().startApp();
    }
}