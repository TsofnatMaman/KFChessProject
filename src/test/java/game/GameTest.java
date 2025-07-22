package game;

import board.Board;
import org.junit.jupiter.api.Test;
import player.PlayerCursor;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    void testGameInitialization() {
        Board board = Board.loadFromCSV(8, 8);
        PlayerCursor c1 = new PlayerCursor(0, 0, Color.RED);
        PlayerCursor c2 = new PlayerCursor(7, 7, Color.BLUE);
        Game game = new Game(board, c1, c2);

        assertNotNull(game.getBoard());
        assertNotNull(game.getPlayer1());
        assertNotNull(game.getPlayer2());
    }
}
