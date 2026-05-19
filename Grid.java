import java.awt.Color;
import java.awt.Graphics;

public class Grid extends Sprite {

    public Grid(int x, int y, int squareSize) {
        super(x, y, squareSize);
    }

    /**
     * Draws the board using an externally passed 2D integer matrix and a 1D color lookup table.
     * * @param g           The graphics context to draw with.
     * @param board       The 2D array of integers (e.g., -1 for empty, 0 for Blue, 1 for Yellow...)
     * @param pieceColors The 1D array of actual Color objects mapped to those index IDs.
     */
    public void draw(Graphics g, int[][] board, Color[] pieceColors) {
        int size = getSquareSize(); 
        
        // Safety check to ensure arrays exist
        if (board == null || pieceColors == null) return;

        int rows = board.length;
        int cols = board[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int drawX = getX() + (c * size);
                int drawY = getY() + (r * size);

                int colorIndex = board[r][c];

                // If the index is valid, look up the color and draw the played piece block
                if (colorIndex >= 0 && colorIndex < pieceColors.length) {
                    Color blockColor = pieceColors[colorIndex];
                    drawSquare(g, drawX, drawY, blockColor);
                } else {
                    // Otherwise, treat it as an empty tile (-1)
                    g.setColor(Color.WHITE);
                    g.fillRect(drawX, drawY, size, size);
                    
                    g.setColor(Color.GRAY);
                    g.drawRect(drawX, drawY, size, size);
                }
            }
        }
    }
}