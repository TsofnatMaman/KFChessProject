package interfaces;

import pieces.Position;

import java.awt.geom.Point2D;
import java.util.Map;

public interface IPiece {

    public int getPlayer() ;

    public String getId();

    public String getType();

    public void setState(EState newStateName) ;

    public IState getCurrentState();

    public void update() ;

    public void move(Position to) ;

    public void jump() ;

    public boolean isCaptured();

    public void markCaptured();

    public void setLogicalPosition(Position pos) ;

    public int getRow();

    public int getCol();

    public EState getCurrentStateName();

    public Point2D.Double getCurrentPixelPosition();

    public Moves getMoves();

    public Map<EState, IState> getStates() ;
}
