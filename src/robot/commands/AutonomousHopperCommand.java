package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import robot.parsable.ParsableDouble;

public class AutonomousHopperCommand extends CommandBase {

    ParsableDouble TIME_AFTER_START = new ParsableDouble("autonomous_time_after_start", 2.0);
    ParsableDouble TIME_AFTER_LAST_FRISBEE = new ParsableDouble("autonomous_time_after_last_frisbee", 1.0);
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
        //Dont wait if we dont want to stop
        if (dontStop || Timer.getFPGATimestamp() - startTime > TIME_AFTER_START.get()) {
            boolean requestShot = shooterSubsystem.onTargetAndAboveThreshold() && hopperSubsystem.hasFrisbee();

            hopperSubsystem.update(requestShot);
        }
        
        if (hopperSubsystem.hasFrisbee()) {
            lastFrisbeeTime = Timer.getFPGATimestamp();
        }
    }

    protected boolean isFinished() {
        if (!DriverStation.getInstance().isAutonomous()) {
            Scheduler.getInstance().add(new TeleopHopperCommand());
            return true;
        }

        if (dontStop) {
            return false;
        } else {
            return Timer.getFPGATimestamp() - lastFrisbeeTime > TIME_AFTER_LAST_FRISBEE.get();
        }
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}