package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import robot.Constants;

public class AimCommand extends CommandBase {

    public static double lastTargetAngle = 0.0;
    double pidGyroNotOnTargetTime = 0.0;

    public AimCommand() {
        requires(chassisSubsystem);
    }

    protected void initialize() {
        chassisSubsystem.shift(false); //Stay in low gear

        chassisSubsystem.enablePID();
        chassisSubsystem.enablePIDGyro();

        System.out.println("Starting Aim Command");
    }

    protected void execute() {
        double targetAngle = Constants.AUTO_AIM_TARGET_ANGLE.get();

        //Only set the setpoint if it's changed from the last setpoint we saw
        if (lastTargetAngle != targetAngle) {
            chassisSubsystem.pidGyroSetpoint(targetAngle);
        }
        lastTargetAngle = targetAngle;

        chassisSubsystem.drive(0.0, -chassisSubsystem.pidGyro.get()); //pidGyro output is opposite

        if (!chassisSubsystem.pidGyroOnTarget()) {
            pidGyroNotOnTargetTime = Timer.getFPGATimestamp();
        }
    }

    protected boolean isFinished() {
        //If teleop and we no longer want to auto aim
        if (DriverStation.getInstance().isOperatorControl() && !oi.getAutoAim()) {
            return true;
        }

        //If we've been on target for AUTO_AIM_ON_TARGET_TIME seconds, then end
        return Timer.getFPGATimestamp() - pidGyroNotOnTargetTime > Constants.AUTO_AIM_ON_TARGET_TIME.get();
    }

    protected void end() {
        chassisSubsystem.disablePID();
        chassisSubsystem.disablePIDGyro();
        if (DriverStation.getInstance().isOperatorControl()) {
            Scheduler.getInstance().add(new TeleopDriveCommand());
        }
        System.out.println("Finishing Aim Command");
    }

    protected void interrupted() {
        end();
    }
}
