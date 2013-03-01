package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;

public class HangerCommand extends CommandBase {

    protected void initialize() {
        requires(hangerSubsystem);
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
