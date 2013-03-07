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

        //True is down, false is up
        if(oi.getPickupLowerOverride()) {
            pickupSubsystem.setPneumatic(true);
        } else if(shooterSubsystem.getShooterState() == ShooterSubsystem.ShooterState.LOAD) {
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
