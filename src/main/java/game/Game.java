package game;

import board.Board;
import board.BoardConfig;
import interfaces.ICommand;
import interfaces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game logic and state management.
 */
public class Game implements IGame {
    private final IPlayer player1;
    private final IPlayer player2;
    private List<ICommand> commandQueue;
    private final IBoard board; // The board itself – separate class for logic

    /**
     * Constructs the game with the given board config and players.
     */
    public Game(BoardConfig bc, IPlayer player1, IPlayer player2) {
        this.board = new Board(bc,new IPlayer[]{ player1, player2 });
        this.player1 = player1;
        this.player2 = player2;
        commandQueue = new ArrayList<>();
    }

    /**
     * Adds a command to the queue.
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
            commandQueue.remove(0).execute();
        }
    }

    /**
     * Gets player 1.
     */
    @Override
    public IPlayer getPlayer1() {
        return player1;
    }

    /**
     * Gets player 2.
     */
    @Override
    public IPlayer getPlayer2() {
        return player2;
    }

    /**
     * Gets the game board.
     */
    @Override
    public IBoard getBoard() {
        return board;
    }

    /**
     * Handles selection for the given player.
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
