package robot.commands;

public class AutonomousRotateCommand extends CommandBase {

    double relativeAngle;

    public AutonomousRotateCommand(double relativeAngle) {
        // Use requires() here to declare subsystem dependencies
        requires(chassisSubsystem);

        this.relativeAngle = relativeAngle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        chassisSubsystem.shift(false); //Stay in low gear

        chassisSubsystem.enablePID();
        chassisSubsystem.enablePIDGyro();

        chassisSubsystem.pidGyroSetpoint(relativeAngle);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        chassisSubsystem.drive(0.0, -chassisSubsystem.pidGyro.get());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return chassisSubsystem.pidGyroOnTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
        chassisSubsystem.disablePIDGyro();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
