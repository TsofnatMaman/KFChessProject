package board;

import java.util.List;

public class BoardConfig {
    public final int numRows;
    public final int numCols;
    public final double panelWidth;
    public final double panelHeight;

    public final double tileSize;

    public final List<List<Integer>> rowsOfPlayer = List.of(
            List.of(0, 1), // שחקן 0
            List.of(6, 7)  // שחקן 1
    );

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
