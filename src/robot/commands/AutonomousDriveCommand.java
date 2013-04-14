package robot.commands;

import robot.subsystems.ChassisSubsystem;

public class AutonomousDriveCommand extends CommandBase {

    int relativeCounts;

    public AutonomousDriveCommand(int relativeCounts) {
        requires(chassisSubsystem);

        this.relativeCounts = relativeCounts;
    }

    public AutonomousDriveCommand(double relativeInches) {
        this((int) (relativeInches / ChassisSubsystem.INCHES_PER_ENCODER_COUNT));
    }

    protected void initialize() {
        chassisSubsystem.shift(false); //Stay in low gear
        
        chassisSubsystem.enablePID();
        chassisSubsystem.enablePIDGyro();
        chassisSubsystem.enablePIDCount();

        chassisSubsystem.pidGyroRelativeSetpoint(0.0);
        chassisSubsystem.pidCountRelativeSetpoint(relativeCounts);
    }

    protected void execute() {
        chassisSubsystem.drive(-chassisSubsystem.pidCount.get(), -chassisSubsystem.pidGyro.get());
    }

    protected boolean isFinished() {
        return chassisSubsystem.pidCountOnTarget();
    }

    protected void end() {
        //Give the drive a setpoint of 0.0, so that the PID actually stops rather than going forever
        chassisSubsystem.drive(0.0, 0.0);
        chassisSubsystem.disablePIDGyro();
        chassisSubsystem.disablePIDCount();
    }

    protected void interrupted() {
        end();
    }
}