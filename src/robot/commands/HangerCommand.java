package robot.commands;

public class HangerCommand extends CommandBase {

    public HangerCommand() {
        requires(hangerSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        hangerSubsystem.raiseHanger(oi.getRaiseHanger());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
