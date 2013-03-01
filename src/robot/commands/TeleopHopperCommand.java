package robot.commands;

public class TeleopHopperCommand extends CommandBase {

    public TeleopHopperCommand() {
        requires(hopperSubsystem);
    }

    protected void initialize() {
        hopperSubsystem.reset();
    }

    protected void execute() {
        boolean otherSubsystemsReady = shooterSubsystem.onTargetAndAboveThreshold() && hopperSubsystem.hasFrisbee();
        if (otherSubsystemsReady || oi.getShootOverride()) {
            hopperSubsystem.update(oi.getRequestShot());
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
