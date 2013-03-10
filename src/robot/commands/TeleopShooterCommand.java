package robot.commands;

import robot.subsystems.ShooterSubsystem;

public class TeleopShooterCommand extends CommandBase {

    public TeleopShooterCommand() {
        requires(shooterSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        shooterSubsystem.setShooter(Math.abs(oi.getManualShooterSpeed()));
        //shooterSubsystem.runMotor(oi.getManualShooterSpeed());

        if (oi.getShooterLoad()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.LOAD);
        } else if (oi.getShooterLow()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.LOW);
        } else if (oi.getShooterMedium()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.MEDIUM);
        } else if (oi.getShooterHigh()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.HIGH);
        }

        shooterSubsystem.doShooterState();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
