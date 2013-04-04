package robot.commands;

public class TeleopHopperCommand extends CommandBase {

    public TeleopHopperCommand() {
        requires(hopperSubsystem);
    }

    protected void initialize() {
        hopperSubsystem.reset();
    }

    protected void execute() {
        boolean shooterRunning = shooterSubsystem.aboveShootThreshold();
        boolean otherSubsystemsReady = shooterSubsystem.onTarget() && hopperSubsystem.hasFrisbee();
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
