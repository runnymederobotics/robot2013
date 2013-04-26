package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import robot.Constants;

public class AimCommand extends CommandBase {

    public static double lastTargetAngle = 0.0;
    double oldP, oldI, oldD, oldF;
    double pidGyroNotOnTargetTime = 0.0;

    public AimCommand() {
        requires(chassisSubsystem);
    }

    protected void initialize() {
        chassisSubsystem.shift(false); //Stay in low gear

        this.oldP = chassisSubsystem.pidGyro.getP();
        this.oldI = chassisSubsystem.pidGyro.getI();
        this.oldD = chassisSubsystem.pidGyro.getD();
        this.oldF = chassisSubsystem.pidGyro.getF();
        chassisSubsystem.pidGyro.setP(Constants.CHASSIS_GYRO_ROTATE_P.get());
        chassisSubsystem.pidGyro.setI(Constants.CHASSIS_GYRO_ROTATE_I.get());
        
        chassisSubsystem.enablePID();
        chassisSubsystem.enablePIDGyro();

        System.out.println("Starting Aim Command");
    }

    protected void execute() {
        //This will make the camera shift over to the right by CAMERA_ERROR, or it will make the camera run away
        //from the target. It all depends on the sign of CAMERA_ERROR, must tweak
        double targetAngle = Constants.AUTO_AIM_TARGET_ANGLE.get() - Constants.CAMERA_ERROR.get();

        //Only set the setpoint if it's changed from the last setpoint we saw
        if (lastTargetAngle != targetAngle) {
            chassisSubsystem.pidGyroRelativeSetpoint(targetAngle);
        }
        lastTargetAngle = targetAngle;

        double output = -chassisSubsystem.pidGyro.get();
        int sign = output < 0 ? -1 : 1;
        if(Math.abs(output) < Constants.CHASSIS_MIN_ROTATE_SPEED.get()) {
            output = sign * Constants.CHASSIS_MIN_ROTATE_SPEED.get();
        }
        
        chassisSubsystem.drive(0.0, output); //pidGyro output is opposite

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
        chassisSubsystem.pidGyro.setP(oldP);
        chassisSubsystem.pidGyro.setI(oldI);
        chassisSubsystem.pidGyro.setD(oldD);
        chassisSubsystem.pidGyro.setF(oldF);
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
