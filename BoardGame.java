import java.awt.*;
import java.util.*;
import java.io.*;

public class BoardGame {
    private Grid grid, tutorialGrid;
    private int[][] board, tutorialBoard;
    private ArrayList<Piece> bluePieces, yellowPieces, redPieces, greenPieces, tutorialPieces;
    private ArrayList<ArrayList<Piece>> allPieces;
    private Color[] pieceColors;
    private int playerNum, prevMouseX, prevMouseY;
    private String state;
    private Piece testingPiece;


    public BoardGame() throws IOException {
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

        BufferedReader br = new BufferedReader(new FileReader("Layouts.txt"));
        String line = br.readLine();
        boolean isRowNum = true;
        int rowNum = 0;
        int[][] layout = new int[0][0];

        int x = 0;
        int y = 0;

        while (line != null) {
            if (isRowNum) {
                String[] dimensions = line.split(",");
                layout = new int[Integer.parseInt(dimensions[0])][Integer.parseInt(dimensions[1])];
                isRowNum = false;
                rowNum = 0;
            } else if (line.equals("")) {
                for (int j = 0; j < allPieces.size(); j++) {
                    allPieces.get(j).add(new Piece(x, y, 25, layout, pieceColors[j]));
                    x += 100;
                    if (x >= 900) {
                        x = 0;
                        y += 100;
                    }
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

        tutorialPieces.add(new Piece(525, 125, 25, new int[][] {{0, 1, 1}, {1, 1, 0}, {1, 0, 0}}, Color.GREEN));
        tutorialPieces.add(new Piece(525, 225, 25, new int[][] {{1, 0}, {1, 1}, {1, 0}}, Color.GREEN));
        tutorialPieces.add(new Piece(675, 125, 25, new int[][] {{0, 1, 0}, {0, 1, 0}, {1, 1, 1}}, Color.RED));
        tutorialPieces.add(new Piece(675, 225, 25, new int[][] {{0, 0, 1}, {0, 0, 1}, {1, 1, 1}}, Color.RED));

        testingPiece = new Piece(25, 500, 25, new int[][] {{0, 1, 1}, {1, 1, 0}, {1, 0, 0}}, Color.YELLOW);
        tutorialGrid = new Grid(25, 125, 25);
        tutorialBoard = new int[10][10];
        for (int r = 0; r < tutorialBoard.length; r++) {
            for (int c = 0; c < tutorialBoard[r].length; c++) {
                tutorialBoard[r][c] = -1;
            }
        }
    }

    public void draw(Graphics g) {
        if (state.equals("play")) {
            grid.draw(g, board, pieceColors);
            for (Piece piece : allPieces.get(playerNum)) {
                piece.draw(g);
            }
        } else if (state.equals("rules")) {
            tutorialGrid.draw(g, tutorialBoard, pieceColors);
            for (Piece piece : tutorialPieces) {
                piece.draw(g);
            }
            testingPiece.draw(g);
        }
        
    }

    public void selectPiece(int x, int y, boolean selected) {
        for (Piece piece : allPieces.get(playerNum)) {
            if (selected && piece.containsPoint(x, y)) {
                piece.setSelected(true);
            } else {
                piece.setSelected(false);
            }
        }
        if (selected && testingPiece.containsPoint(x, y)) {
            testingPiece.setSelected(true);
        } else {
            testingPiece.setSelected(false);
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

    public void alignPiece() {
        for (Piece piece : allPieces.get(playerNum)) {
            if (piece.getSelected()) {
                piece.autoAlign();
            }
        }
        if (testingPiece.getSelected()) {
            testingPiece.autoAlign();
        }
    }

    public String getState() {return state;}
    public void setState(String state) {this.state = state;}
}