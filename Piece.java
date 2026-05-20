import java.awt.Color;
import java.awt.Graphics;

public class Piece extends Sprite {
    private int[][] shape; 
    private Color color;
    private boolean selected;

    public Piece(int x, int y, int squareSize, int[][] shape, Color color) {
        super(x, y, squareSize);
        this.shape = shape;
        this.color = color;
        selected = false;
    }
    /*
     * Rotates the piece 90 degrees.
     * @param clockwise true to rotate clockwise, false to rotate counter-clockwise.
     */
    public void rotate(boolean clockwise) {
        int oldRows = shape.length;
        int oldCols = shape[0].length;
        
        // Both directions result in swapped dimensions
        int[][] rotatedShape = new int[oldCols][oldRows];
        
        for (int r = 0; r < oldRows; r++) {
            for (int c = 0; c < oldCols; c++) {
                if (clockwise) {
                    // Clockwise mapping
                    rotatedShape[c][oldRows - 1 - r] = shape[r][c];
                } else {
                    // Counter-clockwise mapping
                    rotatedShape[oldCols - 1 - c][r] = shape[r][c];
                }
            }
        }
        
        this.shape = rotatedShape;
    }
    /**
     * Shifts the sprite's position by a relative offset amount.
     * @param dx The change in X coordinates (delta X)
     * @param dy The change in Y coordinates (delta Y)
     */
    public void move(int dx, int dy) {
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }
    public void draw(Graphics g) {
        int size = getSquareSize();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                if (shape[r][c] == 1) {
                    int drawX = getX() + (c * size);
                    int drawY = getY() + (r * size);
                    
                    drawSquare(g, drawX, drawY, color);
                }
            }
        }
    }

    /**
     * Checks if a specific screen coordinate (mouseX, mouseY) falls inside 
     * any of the active '1' blocks of this piece.
     * * @return true if the point collides with the piece, false otherwise.
     */
    public boolean containsPoint(int mouseX, int mouseY) {
        int size = getSquareSize();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[0].length; c++) {
                // Only check collisions for active segments of the piece
                if (shape[r][c] == 1) {
                    // Calculate the bounding box for this individual square
                    int blockLeft = getX() + (c * size);
                    int blockRight = blockLeft + size;
                    int blockTop = getY() + (r * size);
                    int blockBottom = blockTop + size;

                    // Check if the mouse point is within this block's bounds
                    if (mouseX >= blockLeft && mouseX <= blockRight &&
                        mouseY >= blockTop && mouseY <= blockBottom) {
                        return true; // Collision detected!
                    }
                }
            }
        }
        return false; // Point is not touching any block of this piece
    }

    public boolean getSelected() {return selected;}
    public void setSelected(boolean selected) {this.selected = selected;}
}