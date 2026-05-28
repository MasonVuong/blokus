/*
To-Do:
Full Block Detection
  (Winner and Square Tallying)
Full Block Detection <--- Do this now
Endscreen (Winner and Square Tallying)
Add Banner On Top
*/

import java.awt.*;
import java.util.*;
import java.io.*;

public class BoardGame {
    private Grid grid, tutorialGrid;
    private int[][] board, tutorialBoard;
    private ArrayList<Piece> bluePieces, yellowPieces, redPieces, greenPieces, tutorialPieces;
    private ArrayList<ArrayList<Piece>> allPieces;
    private Color[] pieceColors;
    private String[] playerNames;
    private int playerNum, prevMouseX, prevMouseY;
    private String state;
    private Piece testingPiece;
    private Font headerFont, scoreFont, titleFont, bodyFont;
    private Piece botActivePiece;
    private int botTargetRow, botTargetCol, delay;
    private Screen screen;
    private int[] skippedPlayers;

    public BoardGame(String[] playerNames, Screen screen) throws IOException {
        grid = new Grid(450, 50, 25);

        bluePieces = new ArrayList<>();
        yellowPieces = new ArrayList<>();
        redPieces = new ArrayList<>();
        greenPieces = new ArrayList<>();

        allPieces = new ArrayList<>();
        allPieces.add(bluePieces);
        allPieces.add(yellowPieces);
        allPieces.add(redPieces);
        allPieces.add(greenPieces);

        pieceColors = new Color[] {Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN};
        this.playerNames = playerNames;

        BufferedReader br = new BufferedReader(new FileReader("Layouts.txt"));
        String line = br.readLine();
        boolean isRowNum = true;
        int rowNum = 0;
        int[][] layout = new int[0][0];

        while (line != null) {
            if (isRowNum) {
                String[] dimensions = line.split(",");
                layout = new int[Integer.parseInt(dimensions[0])][Integer.parseInt(dimensions[1])];
                isRowNum = false;
                rowNum = 0;
            } else if (line.equals("")) {
                for (int j = 0; j < allPieces.size(); j++) {
                    allPieces.get(j).add(new Piece(0, 0, 25, layout, pieceColors[j]));
                }
                isRowNum = true;
            } else {
                String[] rowString = line.split(" ");
                for (int j = 0; j < rowString.length; j++) {
                    layout[rowNum][j] = Integer.parseInt(rowString[j]);
                }
                rowNum++;
            }
            line = br.readLine();
            state = "Start";
        }
        if (layout.length != 0) {
            for (int j = 0; j < allPieces.size(); j++) {
                allPieces.get(j).add(new Piece(0, 0, 25, layout, pieceColors[j]));
            }
        }

        for (int i = 0; i < 4; i++) {
            playerNum = i;
            organizePieces();
        }
        playerNum = 0;  

        board = new int[20][20];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = -1;
            }
        }
        playerNum = 0;

        tutorialPieces = new ArrayList<>();
        tutorialPieces.add(new Piece(25, 300, 25, new int[][] {{0, 1, 0}, {0, 1, 0}, {1, 1, 1}}, Color.GREEN));
        tutorialPieces.add(new Piece(75, 225, 25, new int[][] {{0, 0, 1}, {0, 0, 1}, {1, 1, 1}}, Color.GREEN));
        tutorialPieces.add(new Piece(150, 175, 25, new int[][] {{0, 0, 1}, {1, 1, 1}}, Color.GREEN));
        tutorialPieces.add(new Piece(150, 225, 25, new int[][] {{0, 1, 1}, {1, 1, 0}, {1, 0, 0}}, Color.RED));
        tutorialPieces.add(new Piece(175, 300, 25, new int[][] {{1, 0}, {1, 1}, {1, 0}}, Color.RED));
        tutorialPieces.add(new Piece(225, 300, 25, new int[][] {{0, 1}, {0, 1}, {1, 1}}, Color.RED));

        tutorialPieces.add(new Piece(300, 413, 25, new int[][] {{0, 1, 1}, {1, 1, 0}, {1, 0, 0}}, Color.GREEN));
        tutorialPieces.add(new Piece(400, 413, 25, new int[][] {{1, 0}, {1, 1}, {1, 0}}, Color.GREEN));
        tutorialPieces.add(new Piece(300, 513, 25, new int[][] {{0, 1, 0}, {0, 1, 0}, {1, 1, 1}}, Color.RED));
        tutorialPieces.add(new Piece(400, 513, 25, new int[][] {{0, 0, 1}, {0, 0, 1}, {1, 1, 1}}, Color.RED));

        testingPiece = new Piece(25, 500, 25, new int[][] {{0, 1, 1}, {1, 1, 0}, {1, 0, 0}}, Color.YELLOW);
        tutorialGrid = new Grid(25, 125, 25);
        tutorialBoard = new int[10][10];
        for (int r = 0; r < tutorialBoard.length; r++) {
            for (int c = 0; c < tutorialBoard[r].length; c++) {
                tutorialBoard[r][c] = -1;
            }
        }

        titleFont = new Font("Times New Roman", Font.BOLD, 80);
        bodyFont = new Font("Times New Roman", Font.PLAIN, 25);

        this.screen = screen;
        delay = -1;
        skippedPlayers = new int[4];
        for (int i = 0; i < skippedPlayers.length; i++) {
            skippedPlayers[i] = -1;
        }
    }

    public void draw(Graphics g) {
        if (state.equals("play")) {
            grid.draw(g, board, pieceColors);
            if (playerNum < allPieces.size() && !allPieces.get(playerNum).isEmpty()) {
                for (Piece piece : new ArrayList<>(allPieces.get(playerNum))) {
                    piece.draw(g);
                }
            }
            //Draw arrows

            //Blue
            drawArrows(g, grid.getX() - 5 , grid.getY() - 5, Color.BLUE);
            //green
            drawArrows(g, grid.getX() + 20 * grid.getSquareSize() + 5, grid.getY() - 5, Color.GREEN);
            //yellow
            drawArrows(g, grid.getX() - 5 , grid.getY() + 20 * grid.getSquareSize() + 5, Color.YELLOW);
            //Red
            drawArrows(g, grid.getX() + 20 * grid.getSquareSize() + 5, grid.getY() + 20 * grid.getSquareSize() + 5, Color.RED);
            if (delay != -1) {
                delay++;
                if (delay > 10) {
                    delay = -1;
                    state = "endscreen";
                }
            }
        } else if (state.equals("setup")) {
            
        } else if (state.equals("rules")) {
            tutorialGrid.draw(g, tutorialBoard, pieceColors);
            for (Piece piece : tutorialPieces) {
                piece.draw(g);
            }
            testingPiece.draw(g);
            g.setColor(Color.BLACK);
            g.setFont(titleFont);
            g.drawString("Blokus Rules", 250, 80);
            g.setFont(bodyFont);
            g.drawString("4 Players Have Pieces of Their Color", 325, 150);
            g.drawString("Your First Piece Must be In Your Corner", 325, 200);
            g.drawString("Your Other Pieces Must be Corner-To-Corner", 325, 250);
            g.drawString("Your Pieces can Touch Other Players' but not Overlap", 325, 300);
            g.drawString("Once No One can Place a Piece, Squares are Counted", 325, 350);
            g.drawString("The Player with the Least Squares Left Not Placed Wins", 325, 400);

            g.drawString("Use Mouse, E/Q, & WASD", 25, 400);
            g.drawString("Try with This Piece", 25, 450);

            g.setFont(titleFont);
            g.drawString("9 (Winner)", 500, 475);
            g.drawString("10", 500, 575);
        } else if (state.equals("endscreen")) {
            int[] ranks = rankPlayers();
            g.setColor(Color.BLACK);
            g.setFont(titleFont);
            for (int i = 0; i < 4; i++) {
                String text = i + 1 + ". " + playerNames[ranks[i]] + ": " + getSquaresLeft(ranks[i]);
                FontMetrics metrics = g.getFontMetrics(titleFont);
                g.drawString(text, 500 - metrics.stringWidth(text) / 2, 100 + i * 100);
            }
        }
    }

    public void update() {
        if (botActivePiece == null || !state.equals("play")) return;
        if (botActivePiece.stepTowardsTarget()) {
            int[][] layout = botActivePiece.getLayout();
            for (int pr = 0; pr < layout.length; pr++) {
                for (int pc = 0; pc < layout[pr].length; pc++) {
                    if (layout[pr][pc] == 1) {
                        board[botTargetRow + pr][botTargetCol + pc] = playerNum;
                    }
                }
            }
            allPieces.get(playerNum).remove(botActivePiece);
            botActivePiece = null;
            organizePieces();
            passPlay();
        }
    }

    public void selectPiece(int x, int y, boolean selected) {
        for (Piece piece : allPieces.get(playerNum)) {
            if (selected && piece.containsPoint(x, y)) {
                piece.setSelected(true);
            } else {
                piece.setSelected(false);
                piece.resetPosition();
            }
        }
        if (selected && testingPiece.containsPoint(x, y)) {
            testingPiece.setSelected(true);
        } else {
            testingPiece.setSelected(false);
            testingPiece.resetPosition();
        }
        prevMouseX = x;
        prevMouseY = y;
    }

    public void movePiece(int x, int y) {
        for (Piece piece : allPieces.get(playerNum)) {
            if (piece.getSelected()) {
                piece.move(x - prevMouseX, y - prevMouseY);
                prevMouseX = x;
                prevMouseY = y;
            }
        }
        if (testingPiece.getSelected()) {
            testingPiece.move(x - prevMouseX, y - prevMouseY);
            prevMouseX = x;
            prevMouseY = y;
        }
    }

    public void rotatePiece(boolean clockwise) {
        for (Piece piece : allPieces.get(playerNum)) {
            if (piece.getSelected()) {
                piece.rotate(clockwise);
            }
        }
        if (testingPiece.getSelected()) {
            testingPiece.rotate(clockwise);
        }
    }
    
    public void flipPiece(boolean horizontal) {
        for (Piece piece : allPieces.get(playerNum)) {
            if (piece.getSelected()) {
                piece.flip(horizontal);
            }
        }
        if (testingPiece.getSelected()) {
            testingPiece.flip(horizontal);
        }
    }

    public void alignPiece() {
        Piece removed = null;
        for (Piece piece : allPieces.get(playerNum)) {
            if (piece.getSelected()) {
                piece.autoAlign();
                if (isValidMove(piece)) {
                    int xDif = (piece.getX() - grid.getX()) / 25;
                    int yDif = (piece.getY() - grid.getY()) / 25;
                    int[][] layout = piece.getLayout();
                    for (int r = 0; r < layout.length; r++) {
                        for (int c = 0; c < layout[r].length; c++) {
                            if (layout[r][c] == 1) {
                                board[yDif + r][xDif + c] = playerNum;
                            }
                        }
                    }
                    removed = piece;
                } else {
                    piece.resetPosition();
                } 
            }
        }
        if (removed != null) {
            allPieces.get(playerNum).remove(removed);
            organizePieces();
            passPlay();
            
        }
        if (testingPiece.getSelected()) {
            testingPiece.autoAlign();
        }
    }
    
    public boolean isValidMove(Piece piece) {
        if (piece.getX() < grid.getX() || piece.getX() + (piece.getLayout()[0].length * 25) > grid.getX() + 500 || piece.getY() < grid.getY() || piece.getY() + (piece.getLayout().length * 25)  > grid.getY() + 500) {
            //System.out.println("Out of Bounds");
            return false;
        }
        boolean firstMove = true;
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c] == playerNum) {
                    firstMove = false;
                    break;
                }
                if (! firstMove) {
                    break;
                }
            }
        }
        int[][] startingCorners = new int[][] {{grid.getX(), grid.getY()}, {grid.getX(), grid.getY() + 500}, {grid.getX() + 500, grid.getY() + 500}, {grid.getX() + 500, grid.getY()}};
        if (firstMove) {
            if (! piece.containsPoint(startingCorners[playerNum][0], startingCorners[playerNum][1])) {
                //System.out.println("Not Corner Start");
                return false;
            }
            //System.out.println("Valid First Move");
            return true;
        }

        boolean cornerConnect = false;
        int[][] layout = piece.getLayout();
        for (int r = 0; r < layout.length; r++) {
            for (int c = 0; c < layout[r].length; c++) {
                if (layout[r][c] == 0) {
                    continue;
                }
                int pieceRow = r + (piece.getY() - grid.getY()) / 25;
                int pieceCol = c + (piece.getX() - grid.getX()) / 25;
                if (board[pieceRow][pieceCol] != -1) {
                    //System.out.println("Overlap");
                    return false;
                }
                for (int rDif = -1; rDif < 2; rDif++) {
                    for (int cDif = -1; cDif < 2; cDif++) {
                        try {
                            if (rDif == 0 && cDif == 0) {
                                continue;
                            } else if (rDif == 0 || cDif == 0) { //Horizontal check
                                if (board[pieceRow + rDif][pieceCol + cDif] == playerNum) {
                                    //System.out.println("Horizontal to Friendly");
                                    return false;
                                }
                            } else { //Diagonal check
                                if (board[pieceRow + rDif][pieceCol + cDif] == playerNum) {
                                    cornerConnect = true;
                                }
                            }
                        } catch(ArrayIndexOutOfBoundsException e) {
                            //System.out.println("Array Index out of Bounds");
                        }
                    }
                }
            }
        }
        if (! cornerConnect) {
            //System.out.println("Not Corner-To-Corner");
            return false;
        }
        //System.out.println("Valid Regular Move");
        return true;
    }

    public void organizePieces() {
        int x = 25;
        int y = 25;
        int longestY = 0;
        for (Piece piece : allPieces.get(playerNum)) {
            int[][] layout = piece.getLayout();
            if (x + layout[0].length * 25 + 25 >= 425) {
                x = 25;
                y += longestY + 25;
                longestY = 0;
            }
            piece.setPosition(x, y);
            piece.setOgPosition(x, y);
            x += layout[0].length * 25 + 25;
            if (layout.length * 25 > longestY) {
                longestY = layout.length * 25;
            }
        }
    }

   public void drawArrows(Graphics g, int arrowX, int arrowY, Color color) {

    //angle based on color
    int angle = 45; 
    if (color.equals(Color.GREEN))  angle = 135;  
    if (color.equals(Color.RED))    angle = -135; 
    if (color.equals(Color.YELLOW)) angle = -45;  

    // cast g2d
    Graphics2D g2d = (Graphics2D) g.create(); 
    g2d.setColor(color);
    
    // origin goes to cords provided
    g2d.translate(arrowX, arrowY);
    g2d.rotate(Math.toRadians(angle));

    // draw arrow
    g2d.fillRect(-35, -6, 25, 12); 
    
    int[] xPoints = {-10, -10, 0}; 
    int[] yPoints = {-10, 10, 0}; 
    g2d.fillPolygon(xPoints, yPoints, 3);

   
}

    public boolean canMakeMove() {
        for (Piece piece : allPieces.get(playerNum)) {
            for (int flipNum = 0; flipNum < 2; flipNum++) {
                for (int rotNum = 0; rotNum < 4; rotNum++) {
                    for (int r = 0; r < board.length; r++) {
                        for (int c = 0; c < board[r].length; c++) {
                            piece.setPosition(grid.getX() + c * grid.getSquareSize(), grid.getY() + r * grid.getSquareSize());
                            if (isValidMove(piece)) {
                                piece.resetPosition();
                                return true;
                            }
                        }
                    }
                    piece.rotate(true);
                }
                piece.flip(true);
            }
            piece.resetPosition();
        }
        return false;
    }

    public int[] rankPlayers() {
        int[] ranks = new int[] {0, 1, 2, 3};
        for (int i = 0; i < playerNames.length - 1; i++) {
            for (int j = 0; j < playerNames.length - 1 - i; j++) {
                if (getSquaresLeft(ranks[j]) > getSquaresLeft(ranks[j+1])) {
                    int temp = ranks[j];
                    ranks[j] = ranks[j+1];
                    ranks[j+1] = temp;
                }
            }
        }
        return ranks;
    }

    /**
     * Makes a valid move for the current player automatically.
     * Strategy: tries pieces largest-first. For the biggest piece that has
     * any valid placement, picks the placement whose center of mass is
     * closest to the board center (most central positioning).
     */
    public void botMove() {
        if (botActivePiece != null) return; // already animating
        ArrayList<Piece> pieces = allPieces.get(playerNum);
        Collections.shuffle(pieces);
        pieces.sort((a, b) -> b.getSquares() - a.getSquares());

        // Board center in cell coordinates
        double boardCenterR = (board.length    - 1) / 2.0;  // 9.5
        double boardCenterC = (board[0].length - 1) / 2.0;  // 9.5

        for (Piece piece : pieces) {

            // Track the best placement found for this piece
            int      bestRow    = -1;
            int      bestCol    = -1;
            int[][]  bestLayout = null;          // snapshot of the winning orientation
            double   bestDist   = Double.MAX_VALUE;

            for (int flipNum = 0; flipNum < 2; flipNum++) {
                for (int rotNum = 0; rotNum < 4; rotNum++) {
                    for (int r = 0; r < board.length; r++) {
                        for (int c = 0; c < board[r].length; c++) {
                            piece.setPosition(
                                grid.getX() + c * grid.getSquareSize(),
                                grid.getY() + r * grid.getSquareSize()
                            );

                            if (isValidMove(piece)) {
                                // Compute center of mass of this placement in board coords
                                int[][] layout = piece.getLayout();
                                double sumR = 0, sumC = 0, filled = 0;
                                for (int pr = 0; pr < layout.length; pr++) {
                                    for (int pc = 0; pc < layout[pr].length; pc++) {
                                        if (layout[pr][pc] == 1) {
                                            sumR += r + pr;
                                            sumC += c + pc;
                                            filled++;
                                        }
                                    }
                                }
                                double dist = Math.sqrt(
                                    Math.pow((sumR / filled) - boardCenterR, 2) +
                                    Math.pow((sumC / filled) - boardCenterC, 2)
                                );

                                if (dist < bestDist) {
                                    bestDist = dist;
                                    bestRow  = r;
                                    bestCol  = c;
                                    // Deep-copy the layout so the orientation is frozen here
                                    bestLayout = new int[layout.length][];
                                    for (int i = 0; i < layout.length; i++)
                                        bestLayout[i] = layout[i].clone();
                                }
                            }
                        }
                    }
                    piece.rotate(true);
                }
                piece.flip(true);
            }
            piece.resetPosition(); // Restore x/y for tray; shape is already back after 4 rotations + 2 flips

            if (bestLayout != null) {
                piece.setShape(bestLayout);
                piece.setTarget(
                    grid.getX() + bestCol * grid.getSquareSize(),
                    grid.getY() + bestRow * grid.getSquareSize()
                );
                botActivePiece = piece;
                botTargetRow   = bestRow;
                botTargetCol   = bestCol;
                return;
            }
            // No valid placement for this piece — fall through to try the next smaller one
        }
    }

    public void passPlay() {
        int playersTested = 0;
        do {
            playerNum++;
            playersTested++;
            if (playersTested > 4) {
                delay = 0;
                return;
            }
            if (playerNum > 3) {
                playerNum = 0;
            }
        }

        while (!canMakeMove());
        if (playerNames[playerNum].indexOf("Bot") != -1) {
            botMove();
        }
    }

    public int getSquaresLeft() {
        return getSquaresLeft(playerNum);
    }
    public int getSquaresLeft(int player) {
        int total = 0;
        for (Piece piece : allPieces.get(player)) {
            total += piece.getSquares();
        }
        return total;
    }
    public String getState() {return state;}
    public void setState(String state) {
        this.state = state;
        if (state.equals("play") && playerNames[playerNum].indexOf("Bot") != -1) {
            botMove();
        }
    }
}