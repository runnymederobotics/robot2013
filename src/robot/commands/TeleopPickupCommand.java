package robot.commands;

public class TeleopPickupCommand extends CommandBase {

    public TeleopPickupCommand() {
        requires(pickupSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        boolean lowerButton = oi.getPickupLowerButton();

        //False is down, true is up
        if(shooterSubsystem.inLoadState()) {
            pickupSubsystem.setPneumatic(!lowerButton);
        } else {
            pickupSubsystem.setPneumatic(false);
        }
        
        pickupSubsystem.runRoller(lowerButton);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
