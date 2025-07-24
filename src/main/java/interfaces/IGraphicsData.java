package interfaces;

import pieces.Position;

import java.awt.image.BufferedImage;

public interface IGraphicsData {

    public void reset(EState state, Position to) ;

    public void update() ;

    public boolean isAnimationFinished();

    public int getCurrentNumFrame();

    public int getTotalFrames();

    public double getFramesPerSec();

    public boolean isLoop();

    public BufferedImage getCurrentFrame() ;
}
