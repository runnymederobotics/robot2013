package robot;

import robot.parsable.ParsableDouble;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {

    public static ParsableDouble PRINT_DELAY = new ParsableDouble("print_delay", 2.0);
    //PWM outputs
    public static final int LEFT_MOTOR_CHANNEL = 1;
    public static final int RIGHT_MOTOR_CHANNEL = 2;
    public static final int PICKUP_ROLLER_MOTOR_CHANNEL = 3;
    public static final int SHOOTER_MOTOR_CHANNEL = 4;
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
    public static final boolean SHOOTER_A_SINGLE_SOLENOID = false;
    public static final boolean SHOOTER_B_SINGLE_SOLENOID = false;
    /*public static final int SOLENOID_SHIFTER_ONE = 1;
     public static final int SOLENOID_SHIFTER_TWO = 2;
     public static final int SOLENOID_STACK_DROPPER_ONE = 3;
     public static final int SOLENOID_STACK_DROPPER_TWO = 4;
     public static final int SOLENOID_STACK_HOLDER_ONE = 5;
     public static final int SOLENOID_STACK_HOLDER_TWO = 6;
     public static final int SOLENOID_SHOOTER_LOADER_ONE = 7;
     public static final int SOLENOID_SHOOTER_LOADER_TWO = 8;
     public static final int SOLENOID_PICKUP_ONE = 1;
     public static final int SOLENOID_PICKUP_TWO = 2;*/
    //Digital inputs
    public static final int ENC_LEFT_ONE = 1;
    public static final int ENC_LEFT_TWO = 2;
    public static final int ENC_RIGHT_ONE = 3;
    public static final int ENC_RIGHT_TWO = 4;
    public static final int ENC_SHOOTER = 5;
    public static final int COMPRESSOR_DI = 10;
    //Analog inputs
    public static final int POSITIONING_GYRO = 2;
}
