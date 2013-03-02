package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;

public class HangerCommand extends CommandBase {
    
    public HangerCommand() {
        requires(hangerSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        hangerSubsystem.releaseHanger(DriverStation.getInstance().isOperatorControl() && oi.getReleaseHanger());
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
}
