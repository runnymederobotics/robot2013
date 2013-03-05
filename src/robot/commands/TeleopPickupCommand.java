package robot.commands;

public class TeleopPickupCommand extends CommandBase {

    public TeleopPickupCommand() {
        requires(pickupSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        boolean lowerButton = oi.getPickupLower();

        //False is down, true is up
        if(oi.getPickupLowerOverride()) {
            pickupSubsystem.setPneumatic(true);
        } else if(shooterSubsystem.inLoadState()) {
            pickupSubsystem.setPneumatic(lowerButton);
        } else {
            pickupSubsystem.setPneumatic(true);
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
