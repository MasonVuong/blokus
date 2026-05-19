import java.awt.Color;
import java.awt.Graphics;

public class Piece extends Sprite {
    private int[][] shape; 
    private Color color;

    public Piece(int x, int y, int squareSize, int[][] shape, Color color) {
        super(x, y, squareSize);
        this.shape = shape;
        this.color = color;
    }

    /**
     * Rotates the piece 90 degrees clockwise.
     */
    public void rotateClockwise() {
        int oldRows = shape.length;
        int oldCols = shape[0].length;
        
        int[][] rotatedShape = new int[oldCols][oldRows];
        
        for (int r = 0; r < oldRows; r++) {
            for (int c = 0; c < oldCols; c++) {
                rotatedShape[c][oldRows - 1 - r] = shape[r][c];
            }
        }
        
        this.shape = rotatedShape;
    }

    /**
     * Rotates the piece 90 degrees counter-clockwise.
     */
    public void rotateCounterClockwise() {
        int oldRows = shape.length;
        int oldCols = shape[0].length;
        
        int[][] rotatedShape = new int[oldCols][oldRows];
        
        for (int r = 0; r < oldRows; r++) {
            for (int c = 0; c < oldCols; c++) {
                // Map the old columns from right-to-left into the new rows from top-to-bottom
                rotatedShape[oldCols - 1 - c][r] = shape[r][c];
            }
        }
        
        this.shape = rotatedShape;
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
}