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
    
    public static ParsableDouble PRINT_DELAY = new ParsableDouble("PRINT_DELAY", 2.0);
    
    public static ParsableInt LEFT_MOTOR_CHANNEL = new ParsableInt("LEFT_MOTOR_CHANNEL", 1);
    public static ParsableInt RIGHT_MOTOR_CHANNEL = new ParsableInt("RIGHT_MOTOR_CHANNEL", 2);
    
    public static ParsableInt SHIFTER_SOLENOID_ONE = new ParsableInt("SHIFTER_SOLENOID_ONE", 1);
    public static ParsableInt SHIFTER_SOLENOID_TWO = new ParsableInt("SHIFTER_SOLENOID_TWO", 2);
    
    public class Driver {
        public static final int PORT = 1;
        
        //Axes
        public static final int DRIVE_AXIS = 2;
        public static final int ROTATION_AXIS = 3;
        
        //Buttons
        public static final int SHIFT_BUTTON = 8;
    }
    
    public class Operator {
        public static final int PORT = 2;
    }
    
}
