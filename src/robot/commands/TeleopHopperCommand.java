package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import robot.Constants;
import robot.subsystems.ShooterSubsystem;

public class TeleopHopperCommand extends CommandBase {

    double lastOffTargetTime = 0;

    public TeleopHopperCommand() {
        requires(hopperSubsystem);
    }

    protected void initialize() {
        hopperSubsystem.reset();
    }

    protected void execute() {
        boolean shooterRunning = shooterSubsystem.aboveShootThreshold();
        boolean otherSubsystemsReady = shooterSubsystem.onTarget() && hopperSubsystem.hasFrisbee();

        if (shooterSubsystem.getShooterState() == ShooterSubsystem.ShooterState.LOW) {
            double now = Timer.getFPGATimestamp();
            if (!shooterSubsystem.onTarget()) {
                lastOffTargetTime = now;
            }

            //Only enable persistency when doing full court
            if (shooterSubsystem.getShooterState() == ShooterSubsystem.ShooterState.LOW) {
                otherSubsystemsReady = otherSubsystemsReady && now - lastOffTargetTime > Constants.SHOOTER_ON_TARGET_TIME.get();
            }
        }

        //If we want a shot, the shooters running, and the other subsystems are ready, if we care or not
        hopperSubsystem.update(oi.getRequestShot() && shooterRunning && (otherSubsystemsReady || oi.getShootOverride()));
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
