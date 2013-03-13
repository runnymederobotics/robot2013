package robot.commands;

import robot.Constants;
import robot.subsystems.ShooterSubsystem;

public class TeleopShooterCommand extends CommandBase {

    public TeleopShooterCommand() {
        requires(shooterSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        //Only run the shooter if the pickup is down
        if (pickupSubsystem.pickupDown()) {
            if (oi.getPyramidSetpoint()) {
                shooterSubsystem.setShooter(Constants.SHOOTER_PYRAMID_SETPOINT.get());
            } else if (oi.getFeederSetpoint()) {
                shooterSubsystem.setShooter(Constants.SHOOTER_FEEDER_SETPOINT.get());
            } else {
                shooterSubsystem.setShooter(Math.abs(oi.getManualShooterSpeed()));
            }
        }

        if (oi.getShooterLoad()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.LOAD);
        } else if (oi.getShooterLow()) {
            shooterSubsystem.setShooterState(ShooterSubsystem.ShooterState.LOW);
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
