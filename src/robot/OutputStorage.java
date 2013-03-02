package robot;

import edu.wpi.first.wpilibj.SpeedController;

public class OutputStorage implements SpeedController {

    double value;
    
    public OutputStorage() {
        value = 0.0;
    }
    
    public double get() {
        return value;
    }

    public void set(double d, byte b) {
        value = d;
    }

    public void set(double d) {
        value = d;
    }

    public void disable() {
    }

    public void pidWrite(double d) {
        value = d;
    }
    
}
