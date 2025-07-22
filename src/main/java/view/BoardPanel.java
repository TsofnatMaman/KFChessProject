package view;

import board.Board;
import game.Game;
import player.PlayerCursor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class BoardPanel extends JPanel {
    private Game game;
    private BufferedImage boardImage;
    private Board board;

    private PlayerCursor cursor1;
    private PlayerCursor cursor2;

    public BoardPanel(Game game, PlayerCursor pc1, PlayerCursor pc2) {
        this.game = game;
        setPreferredSize(new Dimension(800, 800));
        setFocusable(true);

        loadBoardImage();

        board = new Board(getBoardImageWidth(), getBoardImageHeight()).loadBoardFromCSV();

        // אתחל את הקורסורים במיקומים התחלתיים, למשל
        cursor1 = pc1;
        cursor2 = pc2;

        // טען את התמונה של הלוח (כמו שהיה)
        loadBoardImage();

        // הוסף מאזין למקלדת:
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e);
            }
        });
    }

    public Board getBoard() {
        return board;
    }

    private void loadBoardImage() {
        try {
            URL imageUrl = getClass().getClassLoader().getResource("board/board.png");
            if (imageUrl != null) {
                boardImage = ImageIO.read(imageUrl);
            } else {
                System.err.println("Image not found in resources!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBoardImageWidth() {
        return boardImage != null ? boardImage.getWidth() : -1;
    }

    public int getBoardImageHeight() {
        return boardImage != null ? boardImage.getHeight() : -1;
    }

    private void handleKey(KeyEvent e) {
        int key = e.getKeyCode();

        // שחקן 1 - חצים + ENTER
        switch (key) {
            case KeyEvent.VK_UP:
                cursor1.moveUp();
                break;
            case KeyEvent.VK_DOWN:
                cursor1.moveDown();
                break;
            case KeyEvent.VK_LEFT:
                cursor1.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                cursor1.moveRight();
                break;
            case KeyEvent.VK_ENTER:
                System.out.println("Player 1 pressed ENTER");
                if (game != null) {
                    game.handleSelection(game.getPlayer1());
                }
                break;

        }

        // שחקן 2 - WASD + SPACE
        switch (key) {
            case KeyEvent.VK_W:
                cursor2.moveUp();
                break;
            case KeyEvent.VK_S:
                cursor2.moveDown();
                break;
            case KeyEvent.VK_A:
                cursor2.moveLeft();
                break;
            case KeyEvent.VK_D:
                cursor2.moveRight();
                break;

            case KeyEvent.VK_SPACE:
                System.out.println("Player 2 pressed SPACE");
                if (game != null) {
                    game.handleSelection(game.getPlayer2());
                }
                break;
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ציור הלוח
        if (boardImage != null) {
            g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // ציור הכלים
        if (board != null)
            board.drawAll(g, getWidth(), getHeight());

        // ציור הקורסורים (הריבועים) על הלוח
        if (cursor1 != null) cursor1.draw(g, getWidth(), getHeight());
        if (cursor2 != null) cursor2.draw(g, getWidth(), getHeight());
    }

    public void updateAll() {
        if (board != null)
            board.updateAll();
    }
}