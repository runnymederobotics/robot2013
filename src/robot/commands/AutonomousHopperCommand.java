package robot.commands;

import edu.wpi.first.wpilibj.Timer;
import robot.parsable.ParsableDouble;

public class AutonomousHopperCommand extends CommandBase {

    public static ParsableDouble TIME_AFTER_START = new ParsableDouble("autonomous_time_after_start", 2.0);
    public static ParsableDouble FRISBEE_TIMEOUT = new ParsableDouble("autonomous_frisbee_timeout", 1.0);
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
        if (dontStop || Timer.getFPGATimestamp() - startTime > TIME_AFTER_START.get()) {
            requestShot = shooterSubsystem.onTargetAndAboveThreshold()
                    && hopperSubsystem.hasFrisbee() && pickupSubsystem.pickupDown();
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
            return Timer.getFPGATimestamp() - lastFrisbeeTime > FRISBEE_TIMEOUT.get();
        }
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}