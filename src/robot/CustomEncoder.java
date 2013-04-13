package robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.PIDSource;

public class CustomEncoder extends Counter implements PIDSource {
    
    public CustomEncoder(int channel) {
        super(channel);

        //This requires a change in WPILib to make m_counter public
        //To recompile WPILib: extract sunspotfrcsdk/lib/wpilibj.project.zip
        //Open the extracted project in NetBeans
        //Change "private tCounter m_counter;" in Counter.java to be public
        //Compile and copy the contents of dist/lib to subspotfrcsdk/lib
        
        //Using this creates a ring buffer that is averaged to get a more
        //accurate reading
        //Don't put more than 127
        this.m_counter.writeTimerConfig_AverageSize(1);
    }

    public double pidGet() {
        double period = super.getPeriod();
        
        return 1.0 / ((period == 0) ? 1.0 : period);
    }
}
