package robot.commands;

import edu.wpi.first.wpilibj.Timer;
import robot.Constants;

public class AutonomousPickupCommand extends CommandBase {

    boolean lowerPickup;
    boolean runRoller;
    double startTime = 0;

    public AutonomousPickupCommand(boolean lowerPickup, boolean runRoller) {
        this.lowerPickup = lowerPickup;
        this.runRoller = runRoller;

        requires(pickupSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        pickupSubsystem.runRoller(runRoller, false); //Don't run roller in reverse
        pickupSubsystem.setPneumatic(lowerPickup);

        double now = Timer.getFPGATimestamp();
        //If it's the first run through, record releaseTime, otherwise leave it alone
        startTime = startTime == 0 ? now : startTime;
    }

    protected boolean isFinished() {
        double now = Timer.getFPGATimestamp();
        //If we've waited long enough
        return now - startTime > Constants.AUTONOMOUS_PICKUP_DELAY.get();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}