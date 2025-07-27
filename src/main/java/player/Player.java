package player;

import board.BoardConfig;
import command.JumpCommand;
import command.MoveCommand;
import interfaces.*;
import game.LoadPieces;
import pieces.EPieceType;
import pieces.Position;
import utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game, holding pieces and managing actions.
 */
public class Player implements IPlayer{
    private final int id;
    private String name;
    private final IPlayerCursor cursor;
    private Position pending;
    private static int mone=0;

    private final List<IPiece> pieces;
    private boolean isFailed;

    /**
     * Constructs a Player, initializes pieces and status.
     */
    public Player(String name ,IPlayerCursor pc, BoardConfig bc){
        id = mone++;
        this.cursor = pc;
        pending=null;
        isFailed = false;
        this.name = name;

        pieces = new ArrayList<>();

        for(int i:BoardConfig.rowsOfPlayer.get(id))
            for(int j=0; j<8; j++)
                this.pieces.add(PiecesFactory.createPieceByCode(EPieceType.valueOf(LoadPieces.board[i][j].charAt(0)+""),id,new Position(i, j), bc));

    }

    /**
     * Returns the list of pieces owned by the player.
     */
    @Override
    public List<IPiece> getPieces() {
        return pieces;
    }

    /**
     * Returns the player's ID.
     */
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the player's cursor.
     */
    @Override
    public IPlayerCursor getCursor() {
        return cursor;
    }

    /**
     * Gets the pending position for selection.
     */
    @Override
    public Position getPendingFrom() {
        return pending;
    }

    /**
     * Sets the pending position for selection.
     */

    @Override
    public void setPendingFrom(Position pending) {
        this.pending = pending == null? null : pending.copy();
    }

    /**
     * Returns true if the player has failed (e.g., lost their king).
     */
    @Override
    public boolean isFailed(){
        return isFailed;
    }

    /**
     * Marks a piece as captured and updates player status if king is captured.
     * @param p The piece to mark as captured.
     */
    @Override
    public void markPieceCaptured(IPiece p){
        p.markCaptured();
        if(p.getType() == EPieceType.K)
            isFailed = true;
    }

    /**
     * Handles the selection logic for the player, returning a command if an action is performed.
     * @param board The game board.
     * @return ICommand representing the action, or null if no action.
     */
    @Override
    public ICommand handleSelection(IBoard board){
        Position previous = getPendingFrom();
        Position selected = getCursor().getPosition();

        if (previous == null) {
            if(board.getPlayerOf(board.getPiece(selected)) != id)
                return null;

            if (board.hasPiece(getCursor().getRow(), getCursor().getCol()) && board.getPiece(getCursor().getPosition()).getCurrentStateName().isCanAction())
                setPendingFrom(selected);
            else {
                System.err.println("can not choose piece");
                LogUtils.logDebug("can not choose piece");
            }
        } else {
            setPendingFrom(null);
            if(previous.equals(selected))
                return new JumpCommand(board.getPiece(selected), board);
            return new MoveCommand(previous, selected.copy(), board);
        }

        return null;
    }

    @Override
    public int getScore(){
        return 0;
    }

}