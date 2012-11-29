package pac.man.model;

import pac.man.util.Vector;

public abstract class MovementAlgorithm {
    public static enum Speed {
        SLOW(70, "Slow"), NORMAL(100, "Normal"), FAST(130, "Fast");
        
        private final int gain;
        private final String label;
        private Speed(int gain, String label) {
            this.gain = gain;
            this.label = label;
        }
        public int getGain() {
            return gain;
        }
        public String getLabel() {
            return label;
        }
        
        
    }
    private static Speed speed = Speed.NORMAL;

    public static Speed getSpeed() {
        return speed;
    }

    public static void setSpeed(Speed speed) {
        MovementAlgorithm.speed = speed;
    }

    public abstract Vector computeSpeed(Vector position, Vector currentSpeed, Vector prefeedDirection);
}