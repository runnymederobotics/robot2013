package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import robot.parsable.ParsableDouble;

public class AutonomousHopperCommand extends CommandBase {

    ParsableDouble TIME_AFTER_LAST_FRISBEE = new ParsableDouble("autonomous_time_after_last_frisbee", 1.0);
    double lastFrisbeeTime = 0.0;
    
    public AutonomousHopperCommand() {
    }

    protected void initialize() {
        requires(hopperSubsystem);
    }

    protected void execute() {
        boolean requestShot = shooterSubsystem.onTargetAndAboveThreshold() && hopperSubsystem.hasFrisbee();
        
        hopperSubsystem.update(requestShot);
    }

    protected boolean isFinished() {
        DriverStation ds = DriverStation.getInstance();

        if (!ds.isAutonomous()) {
            Scheduler.getInstance().add(new TeleopPickupCommand());
            return true;
        }
        
        double now = Timer.getFPGATimestamp();
        
        if(hopperSubsystem.hasFrisbee()) {
            lastFrisbeeTime = now;
        }

        return now - lastFrisbeeTime > TIME_AFTER_LAST_FRISBEE.get();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}