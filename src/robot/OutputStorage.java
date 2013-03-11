package robot;

import edu.wpi.first.wpilibj.SpeedController;

public class OutputStorage implements SpeedController {

    double value = 0.0;

    public OutputStorage() {
    }

    public double get() {
        return value;
    }

    public void set(double d, byte b) {
        set(d);
    }

    public void set(double d) {
        value = d;
    }

    public void disable() {
    }

    public void pidWrite(double d) {
        set(d);
    }
}
