package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Scheduler;

public class AutonomousDriveCommand extends CommandBase {

    int counts;

    public AutonomousDriveCommand(int counts) {
        this.counts = counts;
    }

    protected void initialize() {
        requires(chassisSubsystem);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        DriverStation ds = DriverStation.getInstance();

        if (!ds.isAutonomous()) {
            Scheduler.getInstance().add(new TeleopDriveCommand());
            return true;
        }

        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}