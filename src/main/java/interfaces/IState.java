package interfaces;

import pieces.Position;

import java.awt.*;
import java.awt.geom.Point2D;

public interface IState {

    public void reset(EState state,Position from, Position to) ;

    public void update() ;

    public boolean isActionFinished() ;

    public int getStartCol();

    public int getStartRow();

    public Point2D.Double getCurrentPosition();

    public Point getBoardPosition();

    public int getCurrentRow();

    public int getCurrentCol();

    public int getTargetRow();

    public int getTargetCol();

    public IPhysicsData getPhysics();

    public IGraphicsData getGraphics();
}
