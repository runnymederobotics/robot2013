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
            chassisSubsystem.shift(oi.getShiftHighGear());


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
