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

        //True is down, false is up
        if (lowerOverride) {
            pickupSubsystem.setPneumatic(true);
        } else if (shooterSubsystem.getShooterState() == ShooterSubsystem.ShooterState.LOAD) {
            pickupSubsystem.setPneumatic(lowerButton);
        } else {
            pickupSubsystem.setPneumatic(true);
        }

        //Reverse the motor when the driver doesn't want to drive the roller,
        //forces the pickup down, and when the operator wants to reverse
        boolean reverse = !lowerButton && lowerOverride && oi.getReversePickup();

        pickupSubsystem.runRoller(lowerButton, reverse);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
