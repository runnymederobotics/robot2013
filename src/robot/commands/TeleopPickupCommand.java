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

        //If we start picking frisbees up, make sure the shooter is in LOAD state
        if(lowerButton) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.LOAD);
        }
        
        //True is down, false is up
        if (lowerOverride) {
            //Allow us to force the pickup down
            pickupSubsystem.setPneumatic(true);
        } else if (shooterSubsystem.getShooterState() == ShooterSubsystem.ShooterState.LOAD) {
            //This is the only state where pneumatic can be set to false
            pickupSubsystem.setPneumatic(lowerButton);
        } else {
            //This forces the pickup down when the shooter is in low or high state
            pickupSubsystem.setPneumatic(true);
        }

        //Reverse the motor when the pickup is lowered and the operator wants to reverse
        boolean reverse = pickupSubsystem.pickupDown() && reversePickup;
        //If we want to reverse, runRoller must be true
        boolean runRoller = lowerButton || reverse;
        
        pickupSubsystem.setDisablePickupRoller(oi.getDisablePickupRoller());
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
