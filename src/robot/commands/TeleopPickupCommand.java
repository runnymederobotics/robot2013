package robot.commands;

import robot.subsystems.ShooterSubsystem;

public class TeleopPickupCommand extends CommandBase {

    public TeleopPickupCommand() {
        requires(pickupSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        boolean lowerButton = oi.getPickupLower();
        boolean lowerOverride = oi.getPickupLowerOverride();
        boolean reversePickup = oi.getReversePickup();

        //True is down, false is up
        if (lowerOverride) {
            pickupSubsystem.setPneumatic(true);
        } else if (shooterSubsystem.getShooterState() == ShooterSubsystem.ShooterState.LOAD) {
            pickupSubsystem.setPneumatic(lowerButton);
        } else {
            pickupSubsystem.setPneumatic(true);
        }

        //Reverse the motor when the pickup is lowered and the operator wants to reverse
        boolean reverse = pickupSubsystem.pickupDown() && reversePickup;
        //If we want to reverse, runRoller must be true
        boolean runRoller = lowerButton || reverse;
        
        pickupSubsystem.runRoller(runRoller, reverse);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
