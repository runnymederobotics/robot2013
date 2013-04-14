package robot.commands;

import robot.Constants;

public class AutonomousRotateCommand extends CommandBase {
    
    double oldP, oldI, oldD, oldF;
    boolean relativeAngle;
    double angle;
    
    public AutonomousRotateCommand(boolean relativeAngle, double angle) {
        // Use requires() here to declare subsystem dependencies
        requires(chassisSubsystem);
        
        this.relativeAngle = relativeAngle;
        this.angle = angle;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        chassisSubsystem.shift(false); //Stay in low gear

        this.oldP = chassisSubsystem.pidGyro.getP();
        this.oldI = chassisSubsystem.pidGyro.getI();
        this.oldD = chassisSubsystem.pidGyro.getD();
        this.oldF = chassisSubsystem.pidGyro.getF();
        chassisSubsystem.pidGyro.setP(Constants.CHASSIS_GYRO_ROTATE_P.get());
        
        chassisSubsystem.enablePID();
        chassisSubsystem.enablePIDGyro();
        
        if (relativeAngle) {
            chassisSubsystem.pidGyroRelativeSetpoint(angle);
        } else {
            chassisSubsystem.pidGyroAbsoluteSetpoint(angle);
        }
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
        chassisSubsystem.pidGyro.setP(oldP);
        chassisSubsystem.pidGyro.setI(oldI);
        chassisSubsystem.pidGyro.setD(oldD);
        chassisSubsystem.pidGyro.setF(oldF);
        
        chassisSubsystem.disablePIDGyro();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
