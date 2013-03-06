package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import robot.parsable.ParsableDouble;

public class AutonomousHopperCommand extends CommandBase {

    ParsableDouble TIME_AFTER_LAST_FRISBEE = new ParsableDouble("autonomous_time_after_last_frisbee", 1.0);
    double lastFrisbeeTime = 0.0;
    
    public AutonomousHopperCommand() {
        requires(hopperSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        boolean requestShot = shooterSubsystem.onTargetAndAboveThreshold() && hopperSubsystem.hasFrisbee();
        
        hopperSubsystem.update(requestShot);
        
        if(hopperSubsystem.hasFrisbee()) {
            lastFrisbeeTime = Timer.getFPGATimestamp();
        }
    }

    protected boolean isFinished() {
        if (!DriverStation.getInstance().isAutonomous()) {
            Scheduler.getInstance().add(new TeleopHopperCommand());
            return true;
        }
        
        return Timer.getFPGATimestamp() - lastFrisbeeTime > TIME_AFTER_LAST_FRISBEE.get();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}