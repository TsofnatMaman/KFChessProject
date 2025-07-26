package board;

import java.util.List;

/**
 * Configuration for the game board, including dimensions and tile size.
 */
public class BoardConfig {
    public final int numRows;
    public final int numCols;
    
    /** Width of the board panel in pixels. */
    public final double panelWidth;
    /** Height of the board panel in pixels. */
    public final double panelHeight;
    /** Size of a single tile in pixels. */
    public final double tileSize;

    /**
     * Defines which rows belong to each player.
     * Player 0: rows 0, 1; Player 1: rows 6, 7.
     */
    public static final List<List<Integer>> rowsOfPlayer = List.of(
            List.of(0, 1), // Player 0
            List.of(6, 7)  // Player 1
    );

    /**
     * Constructs a BoardConfig with the specified dimensions.
     * Calculates tile size based on panel size and board dimensions.
     *
     * @param numRows Number of rows on the board
     * @param numCols Number of columns on the board
     * @param panelWidth Width of the board panel in pixels
     * @param panelHeight Height of the board panel in pixels
     */
    public BoardConfig(int numRows, int numCols, double panelWidth, double panelHeight) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;

        double tileW = panelWidth / numCols;
        double tileH = panelHeight / numRows;
        this.tileSize = Math.min(tileW, tileH);
    }
}
