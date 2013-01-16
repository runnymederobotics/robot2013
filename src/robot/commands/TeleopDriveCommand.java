package robot.commands;

import robot.Toggle;

public class TeleopDriveCommand extends CommandBase {

    Toggle toggleTankDrive = new Toggle(false);
    
    public TeleopDriveCommand() {
        // Use requires() here to declare subsystem dependencies
        requires(chassisSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(toggleTankDrive.update(oi.getTankDriveToggleButton())) {
            chassisSubsystem.arcadeDrive(oi.getArcadeDriveDriveAxis(), oi.getArcadeDriveRotationAxis());
        } else {
            chassisSubsystem.tankDrive(oi.getTankDriveLeftSpeed(), oi.getTankDriveRightSpeed());
        }
        
        chassisSubsystem.shift(oi.getShiftButton());
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
