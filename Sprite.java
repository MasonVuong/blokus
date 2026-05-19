import java.awt.Color;
import java.awt.Graphics;

public abstract class Sprite {
    private int x;
    private int y;
    private int squareSize;

    public Sprite(int x, int y, int squareSize) {
        this.x = x;
        this.y = y;
        this.squareSize = squareSize;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSquareSize() { return squareSize; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void drawSquare(Graphics g, int blockX, int blockY, Color color) {
        g.setColor(color);
        g.fillRect(blockX, blockY, squareSize, squareSize);
        
        g.setColor(Color.BLACK);
        g.drawRect(blockX, blockY, squareSize, squareSize);
    }
    
    // Removed the abstract draw(Graphics g) method to allow Grid and Piece 
    // to have completely tailored parameter inputs.
}