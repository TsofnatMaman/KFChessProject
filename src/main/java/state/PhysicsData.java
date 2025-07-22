package state;

public class PhysicsData {
    private double speedMetersPerSec;
    private String nextStateWhenFinished;

    public PhysicsData(double speedMetersPerSec, String nextStateWhenFinished) {
        this.speedMetersPerSec = speedMetersPerSec;
        this.nextStateWhenFinished = nextStateWhenFinished;
    }

    public double getSpeedMetersPerSec() {
        return speedMetersPerSec;
    }

    public void setSpeedMetersPerSec(double speedMetersPerSec) {
        this.speedMetersPerSec = speedMetersPerSec;
    }

    public String getNextStateWhenFinished() {
        return nextStateWhenFinished;
    }

    public void setNextStateWhenFinished(String nextStateWhenFinished) {
        this.nextStateWhenFinished = nextStateWhenFinished;
    }

    public void reset(String state, int[] to) {
        // במידת הצורך לאתחול פרטי
    }

    public void update() {
        // אין צורך כרגע – אבל יכול לשמש לאפקטים פיזיקליים מתקדמים
    }
}
