package robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDSource;

public class CustomEncoder extends Counter implements PIDSource {

    public CustomEncoder(int channel) {
        super(channel);
    }

    public double pidGet() {
        double period = super.getPeriod();
        
        return 1.0 / ((period == 0) ? 1.0 : super.getPeriod());
    }
}
