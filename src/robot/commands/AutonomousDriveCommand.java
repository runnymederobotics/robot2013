package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Scheduler;

public class AutonomousDriveCommand extends CommandBase {

    int relativeCounts;

    public AutonomousDriveCommand(int relativeCounts) {
        requires(chassisSubsystem);
        
        this.relativeCounts = relativeCounts;
    }

    protected void initialize() {
        chassisSubsystem.pidGyroSetpoint(0.0);
        chassisSubsystem.pidCountSetpoint(relativeCounts);
    }

    protected void execute() {
        chassisSubsystem.enablePID();
        chassisSubsystem.enablePIDGyro();
        chassisSubsystem.enablePIDCount();
        
        System.out.println("PIDCount on target: " + chassisSubsystem.pidCountOnTarget());
        
        chassisSubsystem.drive(-chassisSubsystem.pidCount.get(), -chassisSubsystem.pidGyro.get());
    }

    protected boolean isFinished() {
        if (!DriverStation.getInstance().isAutonomous()) {
            Scheduler.getInstance().add(new TeleopDriveCommand());
            return true;
        }
        
        boolean finished = chassisSubsystem.pidCountOnTarget();
        
        if(finished) {
            chassisSubsystem.drive(0.0, 0.0);
        }
        
        return finished;
    }

    protected void end() {
        chassisSubsystem.disablePID();
        chassisSubsystem.disablePIDGyro();
        chassisSubsystem.disablePIDCount();
    }

    protected void interrupted() {
        chassisSubsystem.disablePID();
        chassisSubsystem.disablePIDGyro();
        chassisSubsystem.disablePIDCount();
    }
}