package templates;

import RobotCLI.WebServer;
import java.util.Enumeration;
import java.util.Hashtable;

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

    public static class ConstantHandler implements WebServer.Handler {
        public String handle(Hashtable params) {
            Enumeration keys = params.keys();
            
            String ret = "";
            
            while(keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                double value = Double.parseDouble((String)params.get(key));
                if(key.equals("PRINT_DELAY")) {
                    PRINT_DELAY = value;
                    ret += "PRINT_DELAY = " + PRINT_DELAY;
                }
            }
            return ret;
        }
    }
    
    class Parsable {
        String key;
        double value;
        
        public int toInt() {
            return (int)value;
        }
    }
    
    public static double PRINT_DELAY = 2.0;
    
    public static final int LEFT_MOTOR_CHANNEL = 1;
    public static final int RIGHT_MOTOR_CHANNEL = 2;
    
    public static final int SHIFTER_SOLENOID_ONE = 1;
    public static final int SHIFTER_SOLENOID_TWO = 2;
    
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
