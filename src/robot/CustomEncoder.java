package robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDSource;

public class CustomEncoder extends Counter implements PIDSource {

    public CustomEncoder(int channel) {
        super(channel);
    }

    public double pidGet() {
        return 1.0 / super.getPeriod();
    }
}
