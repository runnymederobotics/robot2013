package robot.commands;

import robot.subsystems.ChassisSubsystem;

public class AutonomousDriveCommand extends CommandBase {

    int relativeCounts;
    
    public AutonomousDriveCommand(int relativeCounts) {
        requires(chassisSubsystem);
        
        this.relativeCounts = relativeCounts;
    }
    
    public AutonomousDriveCommand(double relativeInches) {
        this((int)(relativeInches / ChassisSubsystem.INCHES_PER_ENCODER_COUNT));
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
        boolean finished = chassisSubsystem.pidCountOnTarget();
        
        if(finished) {
            //Give the drive a setpoint of 0.0, so that the PID actually stops rather than going forever
            chassisSubsystem.drive(0.0, 0.0);
        }
        
        return finished;
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