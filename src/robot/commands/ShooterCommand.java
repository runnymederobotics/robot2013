package robot.commands;

public class ShooterCommand extends CommandBase {

    protected void initialize() {
        requires(shooterSubsystem);
    }

    protected void execute() {
        shooterSubsystem.sendEncoder();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
