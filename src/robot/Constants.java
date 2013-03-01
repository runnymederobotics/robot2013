package robot;

import robot.parsable.ParsableDouble;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {

    public static ParsableDouble PRINT_DELAY = new ParsableDouble("print_delay", 1.0);
    //PWM outputs
    public static final int LEFT_MOTOR_CHANNEL = 1;
    public static final int RIGHT_MOTOR_CHANNEL = 2;
    public static final int PICKUP_ROLLER_MOTOR_CHANNEL = 3;
    public static final int SHOOTER_MOTOR_CHANNEL = 4;
    public static final int HOPPER_RELEASE_MOTOR_CHANNEL = 5;
    public static final int HANGER_SERVO = 6;
    //Relay outputs
    public static final int COMPRESSOR_RELAY = 1;
    //Solenoid outputs
    public static final int PRIMARY_MODULE = 1;
    public static final int SECONDARY_MODULE = 2;
    public static final boolean SHIFTER_SINGLE_SOLENOID = false;
    public static final boolean SHOOTER_LOADER_SINGLE_SOLENOID = false;
    public static final boolean STACK_HOLDER_SINGLE_SOLENOID = true;
    public static final boolean STACK_DROPPER_SINGLE_SOLENOID = false;
    public static final boolean PICKUP_SINGLE_SOLENOID = false;
    public static final boolean SHOOTER_LOW_SINGLE_SOLENOID = false;
    public static final boolean SHOOTER_HIGH_SINGLE_SOLENOID = false;
    //Digital inputs
    public static final int ENC_LEFT_ONE = 1;
    public static final int ENC_LEFT_TWO = 2;
    public static final int ENC_RIGHT_ONE = 3;
    public static final int ENC_RIGHT_TWO = 4;
    public static final int ENC_SHOOTER = 5;
    public static final int HOPPER_FRISBEE_SENSOR = 6;
    public static final int PICKUP_FRISBEE_SENSOR = 7;
    public static final int COMPRESSOR_DI = 10;
    //Analog inputs
    public static final int POSITIONING_GYRO = 1;
}
