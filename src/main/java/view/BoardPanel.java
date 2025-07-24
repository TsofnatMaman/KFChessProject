package view;

import interfaces.*;

import board.Board;
import board.BoardRenderer;
import player.PlayerCursor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class BoardPanel extends JPanel {
    private BufferedImage boardImage;
    private final IBoard board;

    private final IPlayerCursor cursor1;
    private final IPlayerCursor cursor2;

    private Consumer<Void> onPlayer1Action;
    private Consumer<Void> onPlayer2Action;

    public BoardPanel(IBoard board, IPlayerCursor pc1, IPlayerCursor pc2) {
        this.board = board;
        this.cursor1 = pc1;
        this.cursor2 = pc2;

        setPreferredSize(new Dimension(800, 800));
        setFocusable(true);

        loadBoardImage();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e);
            }
        });
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

    private void handleKey(KeyEvent e) {
        int key = e.getKeyCode();

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
                if (onPlayer1Action != null) onPlayer1Action.accept(null);
                break;
        }

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
                if (onPlayer2Action != null)
                    onPlayer2Action.accept(null);
                break;
        }

        repaint();
    }

    public void setOnPlayer1Action(Consumer<Void> handler) {
        this.onPlayer1Action = handler;
    }

    public void setOnPlayer2Action(Consumer<Void> handler) {
        this.onPlayer2Action = handler;
    }

    public void updateAll() {
        if (board != null)
            board.updateAll();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (boardImage != null) {
            g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        if (board != null)
            BoardRenderer.draw(g, board, getWidth(), getHeight());

        if (cursor1 != null) cursor1.draw(g, getWidth(), getHeight());
        if (cursor2 != null) cursor2.draw(g, getWidth(), getHeight());
    }
}