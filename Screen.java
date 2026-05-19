import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Screen extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    private BoardGame game;
    private Timer timer;
    private JButton startButton, rulesButton;

    public Screen() throws IOException {
        game = new BoardGame();
        timer = new Timer(50, this);
        timer.start();
        setLayout(null);

        startButton = new JButton("Start");
        startButton.setBounds(450, 400, 100, 50);
        startButton.addActionListener(this);
        add(startButton);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 600);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            System.out.println("Start!");
        }
        if (e.getSource() == timer) {
            repaint();
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}

    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
