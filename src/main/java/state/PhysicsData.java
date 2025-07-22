package state;

public class PhysicsData {
    private double speedMetersPerSec;
    private String nextStateWhenFinished;

    private long startTime;

    public PhysicsData(double speedMetersPerSec, String nextStateWhenFinished) {
        this.speedMetersPerSec = speedMetersPerSec;
        this.nextStateWhenFinished = nextStateWhenFinished;
        reset();
    }

    public void reset() {
        startTime = System.currentTimeMillis();
    }

    public void update() {
        // כאן תוכל להוסיף לוגיקה מתקדמת שקשורה לזמן או פיזיקה
    }

    public double getSpeedMetersPerSec() {
        return speedMetersPerSec;
    }

    public String getNextStateWhenFinished() {
        return nextStateWhenFinished;
    }
}
