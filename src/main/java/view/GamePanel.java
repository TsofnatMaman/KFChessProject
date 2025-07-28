package view;

import events.EventPublisher;
import events.GameEvent;
import events.IEventListener;
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
public class GamePanel extends JPanel implements IEventListener {
    private final BoardPanel boardPanel;
    private final PlayerInfoPanel player1Panel;
    private final PlayerInfoPanel player2Panel;
    private final IGame model;
    private Image backgroundImage;

    public GamePanel(IGame model){
        this.model = model;

        // Set layout with gaps between regions
        setLayout(new BorderLayout(20, 20)); // Spacing between center and sides
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Internal padding from edges

        // Load background image
        try {
            backgroundImage = ImageIO.read(getClass().getClassLoader().getResource("background/background.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load background image: " + e.getMessage());
        }

        // Players info panels
        player1Panel = new PlayerInfoPanel(model.getPlayer1());
        player2Panel = new PlayerInfoPanel(model.getPlayer2());


        Color semiTransparent = new Color(255, 255, 255, 180);
        player1Panel.setBackground(semiTransparent);
        player2Panel.setBackground(semiTransparent);

        // Board
        IPlayerCursor c1 = model.getPlayer1().getCursor();
        IPlayerCursor c2 = model.getPlayer2().getCursor();

        boardPanel = new BoardPanel(model.getBoard(), c1, c2);
        boardPanel.setPreferredSize(new Dimension(700, 700));
        boardPanel.setOpaque(false);

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

        EventPublisher.getInstance().subscribe(GameEvent.GAME_ENDED, this);
    }

    public void run() {
        model.run(boardPanel);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void onEvent(GameEvent event) {
        JOptionPane.showMessageDialog(this, "Game Over. Winner: Player " + model.win().getName());
    }
}
