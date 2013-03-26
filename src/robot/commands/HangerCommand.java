package robot.commands;

import robot.subsystems.ShooterSubsystem;

public class HangerCommand extends CommandBase {

    public HangerCommand() {
        requires(hangerSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        boolean raiseHanger = !chassisSubsystem.getHighGear() && oi.getRaiseHanger();
        
        //Interlock so that hanger can only be raised when in low gear
        hangerSubsystem.setHanger(raiseHanger);
        
        if(raiseHanger) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.LOAD);
            pickupSubsystem.setPneumatic(false);
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
