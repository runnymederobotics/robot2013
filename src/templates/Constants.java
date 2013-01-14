package templates;

import RobotCLI.WebServer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static final int leftMotor = 1;
    // public static final int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static final int rangefinderPort = 1;
    // public static final int rangefinderModule = 1;
    
    public static ParsableDouble PRINT_DELAY = new ParsableDouble("print_delay", 2.0);
    
    public static final int LEFT_MOTOR_CHANNEL = 1;
    public static final int RIGHT_MOTOR_CHANNEL = 2;
    
    public static final int SHIFTER_SOLENOID_ONE = 1;
    public static final int SHIFTER_SOLENOID_TWO = 2;
    
    public static class Driver {
        public static final int PORT = 1;
        
        //Axes
        public static ParsableInt DRIVE_AXIS = new ParsableInt("drive_axis", 2);
        public static ParsableInt ROTATION_AXIS = new ParsableInt("rotation_axis", 3);
        
        //Buttons
        public static ParsableInt SHIFT_BUTTON = new ParsableInt("shift_button", 8);
    }
    
    public static class Operator {
        public static final int PORT = 2;
    }
    
}
