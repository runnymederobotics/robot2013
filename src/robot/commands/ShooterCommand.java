package robot.commands;

public class ShooterCommand extends CommandBase {

    public ShooterCommand() {
        requires(shooterSubsystem);
    }
    
    protected void initialize() {
    }

    protected void execute() {
        shooterSubsystem.runMotor(oi.getManualShooterSpeed());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
