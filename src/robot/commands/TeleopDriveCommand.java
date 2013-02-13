package robot.commands;

import robot.parsable.ParsableDouble;

public class TeleopDriveCommand extends CommandBase {

    ParsableDouble SHIFT_UP_THRESHOLD = new ParsableDouble("auto_shift_up_threshold", 0.9);
    ParsableDouble SHIFT_DOWN_THRESHOLD = new ParsableDouble("auto_shift_down_threshold", 0.5);

    public TeleopDriveCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(chassisSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (oi.getAutoShift()) {
            //Do autoshift
            double rate = chassisSubsystem.getAverageRate();

            //If lowGear && above threshold { shiftUp; }
            //If highGear && below threshold { shiftDown; }
            //Currently set to chassisSubsystem.getShiftState() = false when in low gear
            if (!chassisSubsystem.getShiftState() && Math.abs(rate) > SHIFT_UP_THRESHOLD.get() * chassisSubsystem.MAX_LOW_ENCODER_RATE.get()) {
                chassisSubsystem.shift(true);
            } else if (chassisSubsystem.getShiftState() && Math.abs(rate) < SHIFT_DOWN_THRESHOLD.get() * chassisSubsystem.MAX_HIGH_ENCODER_RATE.get()) {
                chassisSubsystem.shift(false);
            }
        } else {
            chassisSubsystem.shift(oi.getShiftButton());
        }

        chassisSubsystem.drive(oi.getArcadeDriveDriveAxis(), oi.getArcadeDriveRotationAxis());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
