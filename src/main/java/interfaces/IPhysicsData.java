package interfaces;

import pieces.Position;

public interface IPhysicsData {
    public double getSpeedMetersPerSec();

    public void setSpeedMetersPerSec(double speedMetersPerSec);

    public EState getNextStateWhenFinished() ;

    public void setNextStateWhenFinished(EState nextStateWhenFinished) ;

    public void reset(EState state, Position to) ;

    public void update() ;
}
