//package player;
//
//import board.Board;
//import org.junit.jupiter.api.Test;
//
//import java.awt.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class PlayerTest {
//
//    @Test
//    void testPlayerInitialization() {
//        Board board = Board.loadFromCSV(8, 8);
//        PlayerCursor cursor = new PlayerCursor(0, 0, Color.RED);
//        Player player = new Player(cursor);
//
//        assertEquals(cursor, player.getCursor());
//    }
//}
