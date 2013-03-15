package robot.commands;

import edu.wpi.first.wpilibj.Timer;
import robot.Constants;

public class AutonomousHopperCommand extends CommandBase {

    boolean dontStop = false;
    double startTime = 0.0;
    double lastFrisbeeTime = 0.0;

    public AutonomousHopperCommand(boolean dontStop) {
        requires(hopperSubsystem);

        this.dontStop = dontStop;
    }

    protected void initialize() {
        double now = Timer.getFPGATimestamp();
        startTime = now;
        lastFrisbeeTime = now;
    }

    protected void execute() {
        boolean requestShot = false;

        //Dont wait if we dont want to stop
        if (Timer.getFPGATimestamp() - startTime > Constants.AUTONOMOUS_HOPPER_TIME_AFTER_START.get()) {
            requestShot = shooterSubsystem.onTarget() && shooterSubsystem.aboveThreshold() && hopperSubsystem.hasFrisbee() && pickupSubsystem.pickupDown();
        }

        hopperSubsystem.update(requestShot);

        if (hopperSubsystem.hasFrisbee()) {
            lastFrisbeeTime = Timer.getFPGATimestamp();
        }
    }

    protected boolean isFinished() {
        if (dontStop) {
            return false;
        } else {
            return Timer.getFPGATimestamp() - lastFrisbeeTime > Constants.AUTONOMOUS_HOPPER_FRISBEE_TIMEOUT.get();
        }
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}