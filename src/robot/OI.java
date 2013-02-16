package robot;

import edu.wpi.first.wpilibj.Joystick;
import robot.parsable.ParsableDouble;
import robot.parsable.ParsableInt;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    public static class Driver {

        public static final int PORT = 1;
        //Driver Axes
        public static ParsableInt ARCADE_DRIVE_DRIVE_AXIS = new ParsableInt("driver_arcade_drive_drive_axis", 2);
        public static ParsableInt ARCADE_DRIVE_ROTATION_AXIS = new ParsableInt("driver_arcade_drive_rotation_axis", 3);
        public static ParsableInt TANK_DRIVE_LEFT_AXIS = new ParsableInt("driver_tank_drive_left_axis", 2);
        public static ParsableInt TANK_DRIVE_RIGHT_AXIS = new ParsableInt("driver_tank_drive_right_axis", 4);
        //Driver Buttons
        public static ParsableInt SHIFT_BUTTON = new ParsableInt("driver_shift_button", 8);
        public static ParsableInt TOGGLE_AUTO_SHIFT_BUTTON = new ParsableInt("driver_toggle_auto_shift_button", 1);
        public static ParsableInt PICKUP_LOWER_BUTTON = new ParsableInt("pickup_lower_button", 6);
    }

    public static class Operator {

        public static final int PORT = 2;
        //Operator Axes
        public static ParsableInt SHOOTER_AXIS = new ParsableInt("operator_shooter_axis", 3);
        //Operator Buttons
        public static ParsableInt SHOOT_BUTTON = new ParsableInt("operator_shoot_button", 1);
        public static ParsableInt LOAD_STATE_BUTTON = new ParsableInt("operator_load_state_button", 7);
        public static ParsableInt LOW_STATE_BUTTON = new ParsableInt("operator_low_state_button", 8);
        public static ParsableInt MEDIUM_STATE_BUTTON = new ParsableInt("operator_medium_state_button", 9);
        public static ParsableInt HIGH_STATE_BUTTON = new ParsableInt("operator_high_state_button", 10);
    }
    public static final ParsableDouble SHOOTER_MINIMUM_SPEED = new ParsableDouble("shooter_minimum_speed", 0.75);
    Joystick stickDriver = new Joystick(Driver.PORT);
    Joystick stickOperator = new Joystick(Operator.PORT);
    Toggle autoShift = new Toggle(false);

    public boolean getAutoShift() {
        autoShift.update(stickDriver.getRawButton(Driver.TOGGLE_AUTO_SHIFT_BUTTON.get()));
        return autoShift.get();
    }

    public double getArcadeDriveDriveAxis() {
        return stickDriver.getRawAxis(Driver.ARCADE_DRIVE_DRIVE_AXIS.get());
    }

    public double getArcadeDriveRotationAxis() {
        double value = -stickDriver.getRawAxis(Driver.ARCADE_DRIVE_ROTATION_AXIS.get());
        int sign = value >= 0 ? 1 : -1;

        return value * value * sign;
    }

    public double getTankDriveLeftSpeed() {
        return stickDriver.getRawAxis(Driver.TANK_DRIVE_LEFT_AXIS.get());
    }

    public double getTankDriveRightSpeed() {
        return stickDriver.getRawAxis(Driver.TANK_DRIVE_RIGHT_AXIS.get());
    }

    public boolean getShiftButton() {
        return stickDriver.getRawButton(Driver.SHIFT_BUTTON.get());
    }
    
    public boolean getPickupLowerButton() {
        return stickDriver.getRawButton(Driver.PICKUP_LOWER_BUTTON.get());
    }
    
    public boolean getRequestShot() {
        return stickOperator.getRawButton(Operator.SHOOT_BUTTON.get());
    }
    //Only allow greater than -0.8
    final double THROTTLE_DEAD_ZONE = -0.8;

    public double getManualShooterSpeed() {
        //Bottom is -1.0, top is 1.0
        final double complement = (1 - SHOOTER_MINIMUM_SPEED.get()) / 2;
        final double complementsComplement = 1 - complement;
        double axis = -stickOperator.getAxis(Joystick.AxisType.kThrottle);
        double ret = -(axis * complement + complementsComplement);
        //It is now between 0.75 and 1.0
        return axis >= THROTTLE_DEAD_ZONE ? ret : 0.0;
    }
    
    public boolean getShooterLoadButton() {
        return stickOperator.getRawButton(Operator.LOAD_STATE_BUTTON.get());
    }
    
    public boolean getShooterLowButton() {
        return stickOperator.getRawButton(Operator.LOW_STATE_BUTTON.get());
    }
    
    public boolean getShooterMediumButton() {
        return stickOperator.getRawButton(Operator.MEDIUM_STATE_BUTTON.get());
    }
    
    public boolean getShooterHighButton() {
        return stickOperator.getRawButton(Operator.HIGH_STATE_BUTTON.get());
    }
}
