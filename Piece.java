import java.awt.Color;
import java.awt.Graphics;

public class Piece extends Sprite {
    private int[][] shape; 
    private Color color;
    private boolean selected, flipped;
    private int squares, ogX, ogY, rotationAmount;
    // Replace the old target fields with these
    private int    targetX, targetY;
    private double velX, velY;
    private double floatX, floatY;   // sub-pixel accumulator to prevent rounding drift
    private boolean hasTarget = false;
    private float speed;

    public Piece(int x, int y, int squareSize, int[][] shape, Color color) {
        super(x, y, squareSize);
        this.shape = shape;
        this.color = color;
        selected = false;
        ogX = x;
        ogY = y;
        rotationAmount = 0;
        flipped = false;
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                squares += shape[r][c];
            }
        }
        speed = 35;
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
        if (clockwise) {
            rotationAmount += 90;
            if (rotationAmount == 360) {
                rotationAmount = 0;
            }
        } else {
            rotationAmount -= 90;
            if (rotationAmount == -90) {
                rotationAmount = 270;
            }
        }
        
        this.shape = rotatedShape;
    }

        /**
     * Flips the piece 180 degrees over a designated axis.
     * @param horizontal true to flip left-to-right, false to flip top-to-bottom.
     */
    public void flip(boolean horizontal) {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] mirroredShape = new int[rows][cols];
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (horizontal) {
                    // Reverse columns
                    mirroredShape[r][cols - 1 - c] = shape[r][c];
                } else {
                    // Reverse rows
                    mirroredShape[rows - 1 - r][c] = shape[r][c];
                }
            }
        }
        
        this.shape = mirroredShape;
        this.flipped = !this.flipped; // Every flip toggles face-up vs face-down
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

    /** 
     * Sets the destination and immediately computes the X/Y velocity components
     * so the piece travels exactly 5 pixels per frame toward the target.
     */
    public void setTarget(int tx, int ty) {
        this.targetX = tx;
        this.targetY = ty;
        this.hasTarget = true;
        // Initialize the float accumulator from the current integer position
        this.floatX = getX();
        this.floatY = getY();
        // Decompose 5px/frame into X and Y components along the direction vector
        double dx   = tx - getX();
        double dy   = ty - getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) { velX = 0; velY = 0; }
        else           { velX = speed * dx / dist;
                        velY = speed * dy / dist; }
    }

    /**
     * Advances the piece one frame toward its target using the pre-computed velocity.
     * @return true once the piece has arrived
     */
    public boolean stepTowardsTarget() {
        double dx = targetX - floatX;
        double dy = targetY - floatY;
        // Snap once the remaining distance is within one step
        if (Math.sqrt(dx * dx + dy * dy) <= speed) {
            setPosition(targetX, targetY);
            floatX   = targetX;
            floatY   = targetY;
            hasTarget = false;
            return true;
        }
        floatX += velX;
        floatY += velY;
        setX((int) Math.round(floatX));
        setY((int) Math.round(floatY));
        return false;
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

    /**
     * Snaps the piece's X and Y anchor coordinates to the nearest multiple of 25.
     * This aligns the piece perfectly with the board grid lines.
     */
    public void autoAlign() {
        int currentX = getX();
        int currentY = getY();

        // Divide by 25.0 to preserve the decimal, round to nearest integer, scale back up
        int snappedX = (int) Math.round(currentX / 25.0) * 25;
        int snappedY = (int) Math.round(currentY / 25.0) * 25;

        setPosition(snappedX, snappedY);
    }

    public void resetPosition() {
        setX(ogX);
        setY(ogY);
        while (rotationAmount != 0) {
            rotate(false);
        }
        if (flipped) {
            flip(true);
        }
    }
    public boolean getSelected() {return selected;}
    public void setSelected(boolean selected) {this.selected = selected;}
    public int[][] getLayout() {return shape;}
    public void setShape(int[][] layout) {shape = layout;}
    public void setOgPosition(int ogX, int ogY) {
        this.ogX = ogX;
        this.ogY = ogY;
    }
    public boolean isFlipped() {return flipped;}
    public int getSquares() {return squares;}
    public boolean hasTarget() { return hasTarget; }
}