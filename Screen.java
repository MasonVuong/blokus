import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Screen extends JPanel implements ActionListener, MouseListener, MouseMotionListener, KeyListener {
    private BoardGame game;
    private Timer timer;
    private JButton startButton, rulesButton, backButton, restartButton;
    private JTextField blueText, yellowText, redText, greenText;
    private String[] names;
    private boolean blueBot, yellowBot, redBot, greenBot;
    private JButton blueButton, yellowButton, redButton, greenButton;

    public Screen() throws IOException {
        names = new String[] {"Blue", "Yellow", "Red", "Green"};
        blueBot = false;
        yellowBot = true;
        redBot = true;
        greenBot = true;
        game = new BoardGame(names, this);
        timer = new Timer(30, this);
        timer.start();
        setLayout(null);

       


        startButton = new JButton("Start");
        startButton.setBounds(400, 400, 200, 60); 
        startButton.setBorder(BorderFactory.createEtchedBorder()); 

        startButton.addActionListener(this);
        add(startButton);
        startButton.setFocusable(false); 

        startButton.setContentAreaFilled(true);
        startButton.setOpaque(true);


        

        rulesButton = new JButton("Rules");
        rulesButton.setBounds(400, 500, 200, 60);

        rulesButton.setBorder(BorderFactory.createEtchedBorder()); 


        rulesButton.addActionListener(this);
        add(rulesButton);
        rulesButton.setFocusable(false);

        

        backButton = new JButton("Back");
        backButton.setBounds(50, 50, 100, 50);
        backButton.addActionListener(this);
        add(backButton);
        backButton.setFocusable(false);
        backButton.setVisible(false);
        backButton.setBorder(BorderFactory.createEtchedBorder()); 

        restartButton = new JButton("Replay");
        restartButton.setBounds(450, 500, 100, 50);
        restartButton.addActionListener(this);
        add(restartButton);
        restartButton.setFocusable(false);
        restartButton.setVisible(false);

        blueText = new JTextField();
        blueText.setBounds(550, 100, 150, 50);
        add(blueText);
        blueText.setVisible(false);

        yellowText = new JTextField("Yellow Bot");
        yellowText.setBounds(550, 175, 150, 50);
        add(yellowText);
        yellowText.setVisible(false);
        yellowText.setEditable(false);

        redText = new JTextField("Red Bot");
        redText.setBounds(550, 250, 150, 50);
        add(redText);
        redText.setVisible(false);
        redText.setEditable(false);

        greenText = new JTextField("Green Bot");
        greenText.setBounds(550, 325, 150, 50);
        add(greenText);
        greenText.setVisible(false);
        greenText.setEditable(false);

        blueButton = new JButton("Make Bot");
        blueButton.setBounds(400, 100, 150, 50);
        blueButton.addActionListener(this);
        add(blueButton);
        blueButton.setFocusable(false);
        blueButton.setVisible(false);

        yellowButton = new JButton("Make Human");
        yellowButton.setBounds(400, 175, 150, 50);
        yellowButton.addActionListener(this);
        add(yellowButton);
        yellowButton.setFocusable(false);
        yellowButton.setVisible(false);

        redButton = new JButton("Make Human");
        redButton.setBounds(400, 250, 150, 50);
        redButton.addActionListener(this);
        add(redButton);
        redButton.setFocusable(false);
        redButton.setVisible(false);

        greenButton = new JButton("Make Human");
        greenButton.setBounds(400, 325, 150, 50);
        greenButton.addActionListener(this);
        add(greenButton);
        greenButton.setFocusable(false);
        greenButton.setVisible(false);

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
            if (game.getState().equals("start")) {
                game.setState("setup");
                blueText.setVisible(true);
                yellowText.setVisible(true);
                redText.setVisible(true);
                greenText.setVisible(true);
                blueButton.setVisible(true);
                yellowButton.setVisible(true);
                redButton.setVisible(true);
                greenButton.setVisible(true);
            } else {
                game.setNames(new String[] {blueText.getText(), yellowText.getText(), redText.getText(), greenText.getText()});
                game.setState("play");
                startButton.setVisible(false);
                blueText.setVisible(false);
                yellowText.setVisible(false);
                redText.setVisible(false);
                greenText.setVisible(false);
                blueButton.setVisible(false);
                yellowButton.setVisible(false);
                redButton.setVisible(false);
                greenButton.setVisible(false);
            }
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
        } else if (e.getSource() == blueButton) {
            blueBot = !blueBot;
            if (blueBot) {
                blueButton.setText("Make Human");
                blueText.setText("Blue Bot");
                blueText.setEditable(false);
            } else {
               blueButton.setText("Make Bot"); 
               blueText.setText("");
               blueText.setEditable(true);
            }
        } else if (e.getSource() == yellowButton) {
            yellowBot = !yellowBot;
            if (yellowBot) {
                yellowButton.setText("Make Human");
                yellowText.setText("Yellow Bot");
                yellowText.setEditable(false);
            } else {
               yellowButton.setText("Make Bot"); 
               yellowText.setText("");
               yellowText.setEditable(true);
            }
        } else if (e.getSource() == redButton) {
            redBot = !redBot;
            if (redBot) {
                redButton.setText("Make Human");
                redText.setText("Red Bot");
                redText.setEditable(false);
            } else {
               redButton.setText("Make Bot"); 
               redText.setText("");
               redText.setEditable(true);
            }
        } else if (e.getSource() == greenButton) {
            greenBot = !greenBot;
            if (greenBot) {
                greenButton.setText("Make Human");
                greenText.setText("Green Bot");
                greenText.setEditable(false);
            } else {
               greenButton.setText("Make Bot"); 
               greenText.setText("");
               greenText.setEditable(true);
            }
        }else if (e.getSource() == timer) {
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
