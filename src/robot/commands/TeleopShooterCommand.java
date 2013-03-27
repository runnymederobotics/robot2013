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
            switch (shooterSubsystem.getShooterState()) {
                case ShooterSubsystem.ShooterState.LOW:
                    shooterSubsystem.setTolerance(Constants.SHOOTER_FEEDER_TOLERANCE.get());

                    double setpoint = Constants.SHOOTER_FEEDER_SETPOINT.get();

                    if (oi.getEnableFeederAdjustment()) {
                        setpoint += oi.getOperatorThrottle() * Constants.SHOOTER_FEEDER_ADJUSTMENT_MULTIPLIER.get();
                    }

                    shooterSubsystem.setSetpoint(setpoint);
                    break;
                case ShooterSubsystem.ShooterState.HIGH:
                    shooterSubsystem.setTolerance(Constants.SHOOTER_PYRAMID_TOLERANCE.get());
                    shooterSubsystem.setSetpoint(Constants.SHOOTER_PYRAMID_SETPOINT.get());
                    break;
                case ShooterSubsystem.ShooterState.LOAD:
                default:
                    shooterSubsystem.setTolerance(Constants.SHOOTER_PYRAMID_TOLERANCE.get());
                    shooterSubsystem.setSetpoint(Math.abs(oi.getManualShooterSpeed()) * ShooterSubsystem.MAX_SHOOTER_ENCODER_RATE);
                    break;
            }
        } else {
            shooterSubsystem.setSetpoint(0.0);
        }

        if (oi.getDisablePIDShooter()) {
            shooterSubsystem.disable();
        } else {
            shooterSubsystem.enable();
        }

        if (chassisSubsystem.getHighGear() || oi.getShooterLoad()) {
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
