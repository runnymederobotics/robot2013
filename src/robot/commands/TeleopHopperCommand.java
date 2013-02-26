package robot.commands;

public class TeleopHopperCommand extends CommandBase {

    public TeleopHopperCommand() {
        requires(hopperSubsystem);
    }

    protected void initialize() {
        hopperSubsystem.reset();
    }

    protected void execute() {
        if (shooterSubsystem.shooterOnTarget()) {
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
