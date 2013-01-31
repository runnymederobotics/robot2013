package robot.commands;

import edu.wpi.first.wpilibj.Timer;
import robot.parsable.ParsableDouble;

public class PositioningCommand extends CommandBase {

    public static ParsableDouble POSITIONING_RESOLUTION = new ParsableDouble("positioning_resolution", 10.0);
    double lastUpdateTime = 0;

    public PositioningCommand() {
        requires(positioningSubsystem);
    }

    protected void initialize() {
    }

    protected void execute() {
        //Collect the rate constantly so that it can be averaged accurately over a period of time
        chassisSubsystem.updateAverageRate();
        
        double now = Timer.getFPGATimestamp();
        if (now - lastUpdateTime > (1.0 / POSITIONING_RESOLUTION.get())) {
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
