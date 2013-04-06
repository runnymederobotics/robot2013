package robot.commands;

import robot.subsystems.ChassisSubsystem;

public class AutonomousCurveDriveCommand extends CommandBase {

    int relativeCounts;
    double curveValue;

    public AutonomousCurveDriveCommand(int relativeCounts, double curveValue) {
        requires(chassisSubsystem);

        this.relativeCounts = relativeCounts;
        this.curveValue = curveValue;
    }

    public AutonomousCurveDriveCommand(double relativeInches, double curveValue) {
        this((int) (relativeInches / ChassisSubsystem.INCHES_PER_ENCODER_COUNT), curveValue);
    }

    protected void initialize() {
        chassisSubsystem.shift(false); //Stay in low gear
        
        chassisSubsystem.enablePID();
        //chassisSubsystem.enablePIDGyro();
        chassisSubsystem.enablePIDCount();

        //chassisSubsystem.pidGyroRelativeSetpoint(0.0);
        chassisSubsystem.pidCountRelativeSetpoint(relativeCounts);
    }

    protected void execute() {
        chassisSubsystem.drive(-chassisSubsystem.pidCount.get(), curveValue);//-chassisSubsystem.pidGyro.get());
    }

    protected boolean isFinished() {
        boolean finished = chassisSubsystem.pidCountOnTarget();

        if (finished) {
            //Give the drive a setpoint of 0.0, so that the PID actually stops rather than going forever
            chassisSubsystem.drive(0.0, 0.0);
        }

        return finished;
    }

    protected void end() {
        //chassisSubsystem.disablePIDGyro();
        chassisSubsystem.disablePIDCount();
    }

    protected void interrupted() {
        end();
    }
}