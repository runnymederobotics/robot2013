package robot;

import com.sun.squawk.util.MathUtils;
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
        public static ParsableDouble DEAD_ZONE = new ParsableDouble("driver_dead_zone", 0.1);
        //Driver Axes
        public static ParsableInt DRIVE_AXIS = new ParsableInt("driver_drive_axis", 2);
        public static ParsableInt ROTATION_AXIS = new ParsableInt("driver_rotation_axis", 3);
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

    public double getDrive() {
        double axis = stickDriver.getRawAxis(Driver.DRIVE_AXIS.get());
        if (Math.abs(axis) < Driver.DEAD_ZONE.get()) {
            axis = 0.0;
        }
        return axis;
    }

    public double getRotation() {
        double axis = -stickDriver.getRawAxis(Driver.ROTATION_AXIS.get());

        if (Math.abs(axis) < Driver.DEAD_ZONE.get()) {
            axis = 0.0;
        }

        int sign = axis >= 0 ? 1 : -1;

        //Square the axis for less sensitive output
        return MathUtils.pow(axis, 2) * sign;
    }

    public boolean getShift() {
        return stickDriver.getRawButton(Driver.SHIFT_BUTTON.get());
    }

    public boolean getPickupLower() {
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

    public boolean getShooterLoad() {
        return stickOperator.getRawButton(Operator.LOAD_STATE_BUTTON.get());
    }

    public boolean getShooterLow() {
        return stickOperator.getRawButton(Operator.LOW_STATE_BUTTON.get());
    }

    public boolean getShooterMedium() {
        return stickOperator.getRawButton(Operator.MEDIUM_STATE_BUTTON.get());
    }

    public boolean getShooterHigh() {
        return stickOperator.getRawButton(Operator.HIGH_STATE_BUTTON.get());
    }
}
