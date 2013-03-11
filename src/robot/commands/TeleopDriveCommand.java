package robot.commands;

import edu.wpi.first.wpilibj.command.Scheduler;
import robot.Constants;

public class TeleopDriveCommand extends CommandBase {

    public TeleopDriveCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(chassisSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //If we want to auto aim and we have a new targetAngle
        if (oi.getAutoAim() && AimCommand.lastTargetAngle != Constants.AUTO_AIM_TARGET_ANGLE.get()) {
            Scheduler.getInstance().add(new AimCommand());
        } else {
            if (oi.getAutoShift()) {
                //Do autoshift
                double rate = chassisSubsystem.getAverageRate();

                //Allow manual overrides for holding low gear and holding high gear
                if (oi.getShiftLowGear()) {
                    chassisSubsystem.shift(false);
                } else if (oi.getShiftHighGear()) {
                    chassisSubsystem.shift(true);
                } else {
                    //If lowGear && above threshold { shiftUp; }
                    //If highGear && below threshold { shiftDown; }
                    //Currently set to chassisSubsystem.getShiftState() = false when in low gear
                    if (!chassisSubsystem.getShiftState() && Math.abs(rate) > Constants.AUTO_SHIFT_UP_THRESHOLD.get() * Constants.CHASSIS_MAX_LOW_ENCODER_RATE.get()) {
                        chassisSubsystem.shift(true);
                    } else if (chassisSubsystem.getShiftState() && Math.abs(rate) < Constants.AUTO_SHIFT_DOWN_THRESHOLD.get() * Constants.CHASSIS_MAX_LOW_ENCODER_RATE.get()) {
                        chassisSubsystem.shift(false);
                    }
                }
            } else {
                chassisSubsystem.shift(oi.getShiftHighGear());
            }

            if (oi.getEnableChassisPID()) {
                chassisSubsystem.enablePID();
            } else {
                chassisSubsystem.disablePID();
            }

            chassisSubsystem.drive(oi.getDrive(), oi.getRotation());
        }
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
