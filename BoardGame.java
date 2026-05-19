import java.awt.*;
import java.util.*;
import java.io.*;

public class BoardGame {
    private Grid grid;
    private int[][] board;
    private ArrayList<Piece> bluePieces, yellowPieces, redPieces, greenPieces;
    private ArrayList<ArrayList<Piece>> allPieces;
    private Color[] pieceColors;


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
        }

        board = new int[20][20];
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = -1;
            }
        }
    }

    public void draw(Graphics g) {
        for (Piece piece : redPieces) {
            piece.draw(g);
        }
        grid.draw(g, board, pieceColors);
    }
}