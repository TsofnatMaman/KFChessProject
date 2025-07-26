package game;

import board.Board;
import board.BoardConfig;
import interfaces.ICommand;
import interfaces.*;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Main game logic and state management.
 * Handles command execution, player turns, and win condition.
 */
public class Game implements IGame {
    /** Player 1 instance. */
    private final IPlayer player1;
    /** Player 2 instance. */
    private final IPlayer player2;
    /** Queue of commands to be executed. */
    private Queue<ICommand> commandQueue;
    /** The board instance for the game. */
    private final IBoard board;

    /**
     * Constructs the game with the given board config and players.
     * Initializes the board and command queue.
     *
     * @param bc Board configuration
     * @param player1 First player
     * @param player2 Second player
     */
    public Game(BoardConfig bc, IPlayer player1, IPlayer player2) {
        this.board = new Board(bc,new IPlayer[]{ player1, player2 });
        this.player1 = player1;
        this.player2 = player2;
        commandQueue = new LinkedList<>();
    }

    /**
     * Adds a command to the queue.
     * @param cmd The command to add
     */
    @Override
    public void addCommand(ICommand cmd){
        commandQueue.add(cmd);
    }

    /**
     * Executes all commands in the queue.
     */
    @Override
    public void update() {
        while (!commandQueue.isEmpty()) {
            commandQueue.poll().execute();
        }
    }

    /**
     * Gets player 1.
     * @return The first player
     */
    @Override
    public IPlayer getPlayer1() {
        return player1;
    }

    /**
     * Gets player 2.
     * @return The second player
     */
    @Override
    public IPlayer getPlayer2() {
        return player2;
    }

    /**
     * Gets the game board.
     * @return The board instance
     */
    @Override
    public IBoard getBoard() {
        return board;
    }

    /**
     * Handles selection for the given player.
     * Adds the resulting command to the queue if not null.
     * @param player The player making a selection
     */
    @Override
    public void handleSelection(IPlayer player){
        ICommand cmd = player.handleSelection(getBoard());
        if(cmd != null){
            addCommand(cmd);
        }
    }

    /**
     * Returns the winner: 0 for player 1, 1 for player 2, -1 if no winner yet.
     * @return The winner's player index, or -1 if no winner
     */
    @Override
    public int win(){
        if(board.getPlayers()[0].isFailed())
            return 1;
        if(board.getPlayers()[1].isFailed())
            return 0;
        return -1;
    }
}
