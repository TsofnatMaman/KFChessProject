package board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardConfigTest {

    @Test
    void testConstructorCalculatesTileSizeCorrectly() {
        // הגדרת גודל לוח: 8 שורות ו-8 עמודות
        Dimension numRowsCols = new Dimension(8, 8);

        // הגדרת גודל הפאנל (לדוגמה: רוחב 640, גובה 480 פיקסלים)
        Dimension panelSize = new Dimension(640, 480);

        BoardConfig config = new BoardConfig(numRowsCols, panelSize);

        // חישוב ידני של גודל אריח: צריך להיות מינימום של panelHeight/numRows ו-panelWidth/numCols
        double expectedTileWidth = (double) panelSize.getY() / numRowsCols.getY(); // 480/8 = 60
        double expectedTileHeight = (double) panelSize.getX() / numRowsCols.getX(); // 640/8 = 80
        double expectedTileSize = Math.min(expectedTileWidth, expectedTileHeight); // 60

        assertEquals(expectedTileSize, config.tileSize, 0.0001, "Tile size should be the min of width/cols and height/rows");
    }

    @Test
    void testFieldsAreAssignedCorrectly() {
        Dimension numRowsCols = new Dimension(10, 5);
        Dimension panelSize = new Dimension(500, 1000);

        BoardConfig config = new BoardConfig(numRowsCols, panelSize);

        assertEquals(numRowsCols, config.numRowsCols, "numRowsCols should match constructor argument");
        assertEquals(panelSize, config.panelSize, "panelSize should match constructor argument");

        double expectedTileWidth = (double) panelSize.getY() / numRowsCols.getY(); // 1000/5 = 200
        double expectedTileHeight = (double) panelSize.getX() / numRowsCols.getX(); // 500/10 = 50
        double expectedTileSize = Math.min(expectedTileWidth, expectedTileHeight); // 50

        assertEquals(expectedTileSize, config.tileSize, 0.0001, "Tile size calculation should be correct");
    }

    @Test
    void testRowsOfPlayerConstant() {
        assertEquals(2, BoardConfig.rowsOfPlayer.size(), "There should be two players defined");
        assertTrue(BoardConfig.rowsOfPlayer.get(0).contains(0));
        assertTrue(BoardConfig.rowsOfPlayer.get(0).contains(1));
        assertTrue(BoardConfig.rowsOfPlayer.get(1).contains(6));
        assertTrue(BoardConfig.rowsOfPlayer.get(1).contains(7));
    }
}
