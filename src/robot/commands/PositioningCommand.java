package robot.commands;

public class PositioningCommand extends CommandBase {

    public PositioningCommand() {
        requires(positioningSubsystem);
    }
    
    protected void initialize() {
    }

    protected void execute() {
        positioningSubsystem.updateVectors();
        positioningSubsystem.calculatePosition();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
