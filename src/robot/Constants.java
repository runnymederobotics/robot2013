package robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {    
    public static ParsableDouble PRINT_DELAY = new ParsableDouble("print_delay", 2.0);
    
    public static final int LEFT_MOTOR_CHANNEL_ONE = 1;
    public static final int LEFT_MOTOR_CHANNEL_TWO = 2;
    
    public static final int RIGHT_MOTOR_CHANNEL_ONE = 3;
    public static final int RIGHT_MOTOR_CHANNEL_TWO = 4;
    
    public static final int SHIFTER_SOLENOID_ONE = 1;
    public static final int SHIFTER_SOLENOID_TWO = 2;
}
