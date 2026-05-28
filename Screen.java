import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Screen extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    private BoardGame game;
    private Timer timer;
    private JButton startButton, rulesButton, backButton, restartButton;
    private String[] names;

    public Screen() throws IOException {
        names = new String[] {"BotBlue", "BotYellow", "BotRed", "BotGreen"};
        game = new BoardGame(names, this);
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

        restartButton = new JButton("Play Again");
        restartButton.setBounds(450, 500, 100, 50);
        restartButton.addActionListener(this);
        add(restartButton);
        restartButton.setFocusable(false);
        restartButton.setVisible(false);

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
        } else if (e.getSource() == restartButton) {
            try {
                game = new BoardGame(names, this);
            } catch (Exception ex) {
                System.out.println(ex);
            }
            restartButton.setVisible(false);
            startButton.setVisible(true);
            rulesButton.setVisible(true);
        } else if (e.getSource() == timer) {
            game.update();
            repaint();
            if (game.getState().equals("endscreen")) {
                restartButton.setVisible(true);
            }
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
            game.rotatePiece(true); //Clockwise
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            game.rotatePiece(false); //Counterclockwise
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            game.flipPiece(false); //Vertical
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            game.flipPiece(true); //Horizontal
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            game.flipPiece(false); //Vertical
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            game.flipPiece(true); //Horizontal
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            game.setState("endscreen");
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
