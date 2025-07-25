package interfaces;

import pieces.Position;

public interface IPhysicsData {
    public double getSpeedMetersPerSec();

    public void setSpeedMetersPerSec(double speedMetersPerSec);

    public EState getNextStateWhenFinished() ;

    public void setNextStateWhenFinished(EState nextStateWhenFinished) ;

    public void reset(EState state, Position startPos, Position to, double tileSize, long startTimeNanos);

    public void update() ;

    boolean isMovementFinished();

    double getCurrentX();

    double getCurrentY();
}
