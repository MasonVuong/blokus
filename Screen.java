import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Screen extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    private BoardGame game;
    private Timer timer;
    private JButton startButton, rulesButton, backButton;
    private String[] names;

    public Screen() throws IOException {
        names = new String[] {"Blue", "Yellow", "Red", "Green"};
        game = new BoardGame(names);
        timer = new Timer(30, this);
        timer.start();
        setLayout(null);

        startButton = new JButton("Start");
        startButton.setBounds(450, 400, 100, 50);
        startButton.addActionListener(this);
        add(startButton);
        startButton.setFocusable(false); 

        rulesButton = new JButton("Rules");
        rulesButton.setBounds(450, 500, 100, 50);
        rulesButton.addActionListener(this);
        add(rulesButton);
        rulesButton.setFocusable(false);

        backButton = new JButton("Back");
        backButton.setBounds(50, 50, 100, 50);
        backButton.addActionListener(this);
        add(backButton);
        backButton.setFocusable(false);
        backButton.setVisible(false);

        addMouseListener(this);
        addMouseMotionListener(this);
        this.addKeyListener(this);

        this.setFocusable(true);
        this.requestFocusInWindow();
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
            game.setState("play");
            startButton.setVisible(false);
            rulesButton.setVisible(false);
        } else if (e.getSource() == rulesButton) {
            game.setState("rules");
            startButton.setVisible(false);
            rulesButton.setVisible(false);
            backButton.setVisible(true);
        } else if (e.getSource() == backButton) {
            game.setState("start");
            backButton.setVisible(false);
            startButton.setVisible(true);
            rulesButton.setVisible(true);
        } else if (e.getSource() == timer) {
            repaint();
        }
    }

    @Override public void mousePressed(MouseEvent e) {game.selectPiece(e.getX(), e.getY(), true);}
    @Override public void mouseReleased(MouseEvent e) {
        game.alignPiece();
        game.selectPiece(e.getX(), e.getY(), false);
    }
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {game.movePiece(e.getX(), e.getY());}

    @Override public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_E) {
            game.rotatePiece(true);
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            game.rotatePiece(false);
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            System.out.println("Skip");
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
