import Control.MainManager;
import View.MainMenu;

public class Main {
    public static void main(String[] args) {
        MainManager manager = new MainManager(null);
        ((MainMenu) manager.getMenu()).execute();
    }
}