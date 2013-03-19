package robot;

import robot.parsable.ParsableDouble;
import robot.parsable.ParsableInt;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Constants {

    //Robot template
    public static ParsableDouble PRINT_DELAY = new ParsableDouble("print_delay", 1.0);
    public static ParsableInt AUTONOMOUS_MODE = new ParsableInt("autonomous_mode", 0);
    //Positioning subsystem
    public static ParsableDouble POSITIONING_RESOLUTION = new ParsableDouble("positioning_resolution", 30.0);
    //Drive command
    public static ParsableDouble AUTO_SHIFT_UP_THRESHOLD = new ParsableDouble("auto_shift_up_threshold", 0.9);
    public static ParsableDouble AUTO_SHIFT_DOWN_THRESHOLD = new ParsableDouble("auto_shift_down_threshold", 0.5);
    //Aim command
    public static ParsableDouble CAMERA_ERROR = new ParsableDouble("camera_error", 2.0);
    public static ParsableDouble AUTO_AIM_TARGET_ANGLE = new ParsableDouble("auto_aim_target_angle", 0.0);
    public static ParsableDouble AUTO_AIM_ON_TARGET_TIME = new ParsableDouble("auto_aim_on_target_time", 1.0);
    //Chassis subsystem
    public static ParsableDouble CHASSIS_MAX_LOW_ENCODER_RATE = new ParsableDouble("chassis_max_low_encoder_rate", 800);
    public static ParsableDouble CHASSIS_MAX_HIGH_ENCODER_RATE = new ParsableDouble("chassis_max_high_encoder_rate", 2800);
    public static ParsableDouble CHASSIS_ENCODER_MOVEMENT_THRESHOLD = new ParsableDouble("chassis_encoder_movement_threshold", 550);
    //Hopper subsystem
    public static ParsableDouble HOPPER_PNEUMATIC_DELAY = new ParsableDouble("hopper_pneumatic_delay", 0.2);
    public static ParsableDouble HOPPER_RELEASE_DELAY = new ParsableDouble("hopper_shoot_delay", 0.5);
    //Pickup subsystem
    public static ParsableDouble PICKUP_SPEED = new ParsableDouble("pickup_roller_speed", -1.0);
    public static ParsableDouble ELEVATOR_SPEED = new ParsableDouble("elevator_roller_speed", -0.5);
    public static ParsableDouble PICKUP_DELAY_AFTER_FRISBEE = new ParsableDouble("pickup_delay_after_frisbee", 1.0);
    //Shooter subsystem
    public static ParsableDouble SHOOTER_MIN_SHOOT_THRESHOLD = new ParsableDouble("shooter_min_shoot_threshold", 0.3);
    public static ParsableDouble SHOOTER_PYRAMID_SETPOINT = new ParsableDouble("shooter_pyramid_setpoint", 150); //.78 * 175 (old value)
    public static ParsableDouble SHOOTER_FEEDER_SETPOINT = new ParsableDouble("shooter_feeder_setpoint", 160);
    public static ParsableDouble SHOOTER_PYRAMID_TOLERANCE = new ParsableDouble("shooter_pyramid_tolerance", 3.5);
    public static ParsableDouble SHOOTER_FEEDER_TOLERANCE = new ParsableDouble("shooter_feeder_tolerance", 2);
    //Seven frisbee autonomous
    public static ParsableDouble SEVEN_FRISBEE_DRIVE_FORWARD_INCHES = new ParsableDouble("seven_frisbee_drive_forward_inches", 106);
    //Five frisbee autonomous
    public static ParsableDouble FIVE_FRISBEE_DRIVE_FORWARD_INCHES = new ParsableDouble("five_frisbee_drive_forward_inches", 50);
    //Autonomous hopper command
    public static ParsableDouble AUTONOMOUS_HOPPER_TIME_AFTER_START = new ParsableDouble("autonomous_hopper_time_after_start", 1.0);
    public static ParsableDouble AUTONOMOUS_HOPPER_FRISBEE_TIMEOUT = new ParsableDouble("autonomous_frisbee_timeout", 1.0);
    //Autonomous pickup command
    public static ParsableDouble AUTONOMOUS_PICKUP_DELAY = new ParsableDouble("autonomous_pickup_delay", 1.0);
    //PWM outputs
    public static final int LEFT_MOTOR_CHANNEL = 1;
    public static final int RIGHT_MOTOR_CHANNEL = 2;
    public static final int PICKUP_ROLLER_MOTOR_CHANNEL = 3;
    public static final int SHOOTER_MOTOR_CHANNEL = 4;
    public static final int ELEVATOR_ROLLER_MOTOR_CHANNEL = 5;
    //Relay outputs
    public static final int COMPRESSOR_RELAY = 1;
    //Solenoid outputs
    public static final int PRIMARY_MODULE = 1;
    public static final int SECONDARY_MODULE = 2;
    public static final boolean SHIFTER_SINGLE_SOLENOID = false;
    public static final boolean SHOOTER_LOADER_SINGLE_SOLENOID = false;
    public static final boolean HANGER_PNEUMATIC_SINGLE_SOLENOID = false;
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
