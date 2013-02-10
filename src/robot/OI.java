package robot;

import edu.wpi.first.wpilibj.Joystick;
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
        public static ParsableInt TOGGLE_TANK_DRIVE_BUTTON = new ParsableInt("driver_toggle_tank_drive_button", 1);
        public static ParsableInt PICKUP_LOWER_BUTTON = new ParsableInt("pickup_lower_button", 6);
    }

    public static class Operator {

        public static final int PORT = 2;
        public static ParsableInt SHOOT_BUTTON = new ParsableInt("operator_shoot_button", 1);
        public static ParsableInt SHOOTER_AXIS = new ParsableInt("operator_shooter_axis", 3);
    }
    Joystick stickDriver = new Joystick(Driver.PORT);
    Joystick stickOperator = new Joystick(Operator.PORT);

    public boolean getTankDriveToggleButton() {
        return stickDriver.getRawButton(Driver.TOGGLE_TANK_DRIVE_BUTTON.get());
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

    public boolean getRequestShot() {
        return stickOperator.getRawButton(Operator.SHOOT_BUTTON.get());
    }
    
    public boolean getPickupLowerButton() {
        return stickOperator.getRawButton(Driver.PICKUP_LOWER_BUTTON.get());
    }
    
    final double THROTTLE_DEAD_ZONE = 0.1;
    public double getManualShooterSpeed() {
        double axis = -stickOperator.getAxis(Joystick.AxisType.kThrottle) / 2 + 0.5; //Make it between 0.0 and 1.0
        return axis >= THROTTLE_DEAD_ZONE ? -axis : 0.0;
    }
}
