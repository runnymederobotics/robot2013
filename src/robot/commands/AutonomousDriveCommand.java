package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Scheduler;

public class AutonomousDriveCommand extends CommandBase {

    int relativeCounts;

    public AutonomousDriveCommand(int relativeCounts) {
        this.relativeCounts = relativeCounts;
    }

    protected void initialize() {
        requires(chassisSubsystem);
        
        chassisSubsystem.pidGyroSetpoint(0.0);
        chassisSubsystem.pidCountSetpoint(relativeCounts);
    }

    protected void execute() {
        chassisSubsystem.enablePID();
        chassisSubsystem.enablePIDGyro();
        chassisSubsystem.enablePIDCount();
        
        chassisSubsystem.drive(chassisSubsystem.pidCount.get(), -chassisSubsystem.pidGyro.get());
    }

    protected boolean isFinished() {
        if (!DriverStation.getInstance().isAutonomous()) {
            Scheduler.getInstance().add(new TeleopDriveCommand());
            return true;
        }

        return chassisSubsystem.pidCountOnTarget();
    }

    protected void end() {
        chassisSubsystem.disablePIDGyro();
        chassisSubsystem.disablePIDCount();
    }

    protected void interrupted() {
        chassisSubsystem.disablePIDGyro();
        chassisSubsystem.disablePIDCount();
    }
}