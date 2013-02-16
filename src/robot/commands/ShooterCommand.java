package robot.commands;

import robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends CommandBase {

    public ShooterCommand() {
        requires(shooterSubsystem);
    }
    
    protected void initialize() {
    }

    protected void execute() {
        shooterSubsystem.runMotor(oi.getManualShooterSpeed());
        
        if (oi.getShooterLoadButton()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.LOAD);
        } else if (oi.getShooterLowButton()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.LOW);
        } else if (oi.getShooterMediumButton()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.MEDIUM);
        } else if (oi.getShooterHighButton()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.HIGH);
        }
        
        shooterSubsystem.runShooterStateMachine();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
