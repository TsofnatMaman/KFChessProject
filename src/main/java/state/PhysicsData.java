    package state;

    import interfaces.EState;
    import interfaces.IPhysicsData;
    import pieces.Position;

    public class PhysicsData implements IPhysicsData {
        private double speedMetersPerSec;
        private EState nextStateWhenFinished;

        public PhysicsData(double speedMetersPerSec, EState nextStateWhenFinished) {
            this.speedMetersPerSec = speedMetersPerSec;
            this.nextStateWhenFinished = nextStateWhenFinished;
        }

        public double getSpeedMetersPerSec() {
            return speedMetersPerSec;
        }

        public void setSpeedMetersPerSec(double speedMetersPerSec) {
            this.speedMetersPerSec = speedMetersPerSec;
        }

        public EState getNextStateWhenFinished() {
            return nextStateWhenFinished;
        }

        public void setNextStateWhenFinished(EState nextStateWhenFinished) {
            this.nextStateWhenFinished = nextStateWhenFinished;
        }

        public void reset(EState state, Position to) {
            // במידת הצורך לאתחול פרטי
        }

        public void update() {
            // אין צורך כרגע – אבל יכול לשמש לאפקטים פיזיקליים מתקדמים
        }
    }
