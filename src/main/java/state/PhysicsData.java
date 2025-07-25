    package state;

    import interfaces.EState;
    import interfaces.IPhysicsData;
    import pieces.Position;

    public class PhysicsData implements IPhysicsData {
        private double speedMetersPerSec;
        private EState nextStateWhenFinished;

        private double currentX, currentY;
        private Position startPos;
        private Position targetPos;
        private double tileSize;

        private long startTimeNanos;

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

        public void reset(EState state, Position startPos, Position to, double tileSize, long startTimeNanos) {
            this.currentX = startPos.getCol() * tileSize;
            this.currentY = startPos.getRow() * tileSize;

            this.startPos = startPos;
            this.targetPos = to;
            this.tileSize = tileSize;
            this.startTimeNanos = startTimeNanos;
        }

        public void update() {
            updatePosition();
        }


        private void updatePosition() {
            double speed = getSpeedMetersPerSec();
            long now = System.nanoTime();
            double elapsedSec = (now - startTimeNanos) / 1_000_000_000.0;

            double dx = targetPos.dy(startPos) * tileSize;
            double dy = targetPos.dx(startPos) * tileSize;

            double totalDistance = Math.sqrt(dx * dx + dy * dy);

            if (totalDistance == 0 || speed == 0) return;

            double distanceSoFar = Math.min(speed * elapsedSec, totalDistance);
            double t = distanceSoFar / totalDistance;

            currentX = (startPos.getCol() * tileSize) + dx * t;
            currentY = (startPos.getRow() * tileSize) + dy * t;
        }

        @Override
        public boolean isMovementFinished(){
            if(targetPos == null)
                return false;
            double speed = getSpeedMetersPerSec();
            long now = System.nanoTime();
            double elapsedSec = (now - startTimeNanos) / 1_000_000_000.0;

            double dx = targetPos.dy(startPos) * tileSize;
            double dy = targetPos.dx(startPos) * tileSize;
            double totalDistance = Math.sqrt(dx * dx + dy * dy);

            double distanceSoFar = speed * elapsedSec;
            return distanceSoFar >= totalDistance;
        }

        @Override
        public double getCurrentX() {
            return currentX;
        }

        @Override
        public double getCurrentY() {
            return currentY;
        }
    }
