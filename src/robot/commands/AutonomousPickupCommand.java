package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import robot.parsable.ParsableDouble;

public class AutonomousPickupCommand extends CommandBase {

    public static ParsableDouble AUTONOMOUS_PICKUP_DELAY = new ParsableDouble("autonomous_pickup_delay", 1.0);
    boolean value;
    double startTime = 0;

    public AutonomousPickupCommand(boolean value) {
        this.value = value;
        
        requires(pickupSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        pickupSubsystem.runRoller(value);
        pickupSubsystem.setPneumatic(value);

        double now = Timer.getFPGATimestamp();
        //If it's the first run through, record releaseTime, otherwise leave it alone
        startTime = startTime == 0 ? now : startTime;
    }

    protected boolean isFinished() {
        if (!DriverStation.getInstance().isAutonomous()) {
            Scheduler.getInstance().add(new TeleopPickupCommand());
            return true;
        }
        
        double now = Timer.getFPGATimestamp();
        //If we've waited long enough
        return now - startTime > AUTONOMOUS_PICKUP_DELAY.get();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}