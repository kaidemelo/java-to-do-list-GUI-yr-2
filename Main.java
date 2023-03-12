//Imports
import javax.swing.*;

//Main Class
public class Main {

    //Main Method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI gui = new GUI(500, 500);
                gui.setUpGUI();
            }
        });
    }
}