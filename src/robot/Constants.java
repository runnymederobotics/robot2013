package robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {    
    public static ParsableDouble PRINT_DELAY = new ParsableDouble("print_delay", 2.0);
    
    public static final int LEFT_MOTOR_CHANNEL = 1;
    public static final int RIGHT_MOTOR_CHANNEL = 2;
    
    public static final int SOLENOID_SHIFTER_ONE = 1;
    public static final int SOLENOID_SHIFTER_TWO = 2;
    
    public static final int SOLENOID_STACK_DROPPER_ONE = 3;
    public static final int SOLENOID_STACK_DROPPER_TWO = 4;
    
    public static final int SOLENOID_STACK_HOLDER = 5;
    
    public static final int SOLENOID_SHOOTER_LOADER_ONE = 6;
    public static final int SOLENOID_SHOOTER_LOADER_TWO = 7;
}
