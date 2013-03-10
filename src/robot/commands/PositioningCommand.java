package robot.commands;

import edu.wpi.first.wpilibj.Timer;
import robot.Constants;

public class PositioningCommand extends CommandBase {

    double lastUpdateTime = 0;

    public PositioningCommand() {
        requires(positioningSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        double now = Timer.getFPGATimestamp();
        if (now - lastUpdateTime > (1.0 / Constants.POSITIONING_RESOLUTION.get())) {
            positioningSubsystem.updateVectors();
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
