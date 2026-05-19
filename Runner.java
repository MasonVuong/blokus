import javax.swing.JFrame;
import java.io.IOException;

public class Runner {
    public static void main(String args[]) throws IOException {
        Screen game = new Screen();
        JFrame frame = new JFrame("Blokus");
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}