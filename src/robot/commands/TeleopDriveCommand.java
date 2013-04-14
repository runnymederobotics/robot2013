package robot.commands;

import edu.wpi.first.wpilibj.command.Scheduler;
import robot.Constants;

public class TeleopDriveCommand extends CommandBase {

    int precisionRotationIterations = 0;
    
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

            double rotation = oi.getRotation();
            
            double precisionRotation = oi.getPrecisionRotation();
            
            if(precisionRotationIterations < Constants.CHASSIS_PRECISION_ROTATION_ITERATIONS.get() && precisionRotation != 0) {
                rotation = -precisionRotation * Constants.CHASSIS_PRECISION_ROTATION_BURST.get();
                precisionRotationIterations++;
            } else if(precisionRotation == 0) {
                precisionRotationIterations = 0;
            }
            
            chassisSubsystem.drive(oi.getDrive(), rotation);
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
