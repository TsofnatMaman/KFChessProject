package view;

import interfaces.IGame;
import interfaces.IPlayerCursor;
import utils.LogUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Full game panel with board in center, players on sides, and background.
 */
public class GamePanel extends JPanel {
    private final BoardPanel boardPanel;
    private final PlayerInfoPanel player1Panel;
    private final PlayerInfoPanel player2Panel;
    private final IGame model;
    private Timer timer;
    private Image backgroundImage;

    public GamePanel(IGame model) {
        this.model = model;

        // Set layout with gaps between regions
        setLayout(new BorderLayout(20, 20)); // <-- רווחים בין המרכז לצדדים
        setBorder(new EmptyBorder(20, 20, 20, 20)); // <-- רווח פנימי מהקצוות

        // Load background image
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("background/background.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load background image: " + e.getMessage());
        }

        // Players info panels
        player1Panel = new PlayerInfoPanel(model.getPlayer1());
        player2Panel = new PlayerInfoPanel(model.getPlayer2());


        // צבע שקוף קליל או לבן עם אלפא
        Color semiTransparent = new Color(255, 255, 255, 180);
        player1Panel.setBackground(semiTransparent);
        player2Panel.setBackground(semiTransparent);

        // Board
        IPlayerCursor c1 = model.getPlayer1().getCursor();
        IPlayerCursor c2 = model.getPlayer2().getCursor();

        boardPanel = new BoardPanel(model.getBoard(), c1, c2);
        boardPanel.setPreferredSize(new Dimension(700, 700));
        boardPanel.setOpaque(false); // חשוב: לא לצבוע רקע, כדי לראות את הרקע מתחת

        // Events
        boardPanel.setOnPlayer1Action((v) -> model.handleSelection(model.getPlayer1()));
        boardPanel.setOnPlayer2Action((v) -> model.handleSelection(model.getPlayer2()));

        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boardPanel.requestFocusInWindow();
            }
        });

        SwingUtilities.invokeLater(() -> boardPanel.requestFocusInWindow());

        // Layout
        add(player1Panel, BorderLayout.WEST);
        add(player2Panel, BorderLayout.EAST);
        add(boardPanel, BorderLayout.CENTER);

        LogUtils.logDebug("Initial game state setup");
    }

    public void run() {
        startGameLoop();
    }

    public void startGameLoop() {
        if (timer == null) {
            timer = new Timer(16, e -> {
                if (model.win() == null) {
                    model.update();
                    boardPanel.updateAll();
                    boardPanel.repaint();
                } else {
                    stopGameLoop();
                    LogUtils.logDebug("Game Over. Winner: Player " + model.win().getName());
                    JOptionPane.showMessageDialog(this, "Game Over. Winner: Player " + model.win().getName());
                }
            });
        }
        timer.start();
    }

    public void stopGameLoop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public PlayerInfoPanel getPlayer1Panel() {
        return player1Panel;
    }

    public PlayerInfoPanel getPlayer2Panel() {
        return player2Panel;
    }
}
