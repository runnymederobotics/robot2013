package robot.commands;

public class HangerCommand extends CommandBase {

    public HangerCommand() {
        requires(hangerSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        //Interlock so that hanger can only be raised when in low gear
        hangerSubsystem.setHanger(!chassisSubsystem.getHighGear() && oi.getRaiseHanger());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
