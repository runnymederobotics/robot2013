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
    
    public static final int DRIVER_PORT = 1;

    //Driver Axes
    public static ParsableInt DRIVER_ARCADEDRIVE_DRIVE_AXIS = new ParsableInt("driver.arcadedrive_drive_axis", 2);
    public static ParsableInt DRIVER_ARCADEDRIVE_ROTATION_AXIS = new ParsableInt("driver.arcadedrive_rotation_axis", 3);

    public static ParsableInt DRIVER_TANKDRIVE_LEFT_AXIS = new ParsableInt("driver.tankdrive_left_axis", 2);
    public static ParsableInt DRIVER_TANKDRIVE_RIGHT_AXIS = new ParsableInt("driver.tankdrive_right_axis", 4);
    
    //Driver Buttons
    public static ParsableInt DRIVER_SHIFT_BUTTON = new ParsableInt("driver.shift_button", 8);

    public static final int OPERATOR_PORT = 2;
    
}
