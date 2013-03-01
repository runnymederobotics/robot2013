package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Scheduler;

public class AutonomousHopperCommand extends CommandBase {

    public AutonomousHopperCommand() {
    }

    protected void initialize() {
        requires(hopperSubsystem);
    }

    protected void execute() {
        boolean otherSubsystemsReady = shooterSubsystem.onTargetAndAboveThreshold() && hopperSubsystem.hasFrisbee();
        if (otherSubsystemsReady) {
            hopperSubsystem.update(true);
        }
    }

    protected boolean isFinished() {
        DriverStation ds = DriverStation.getInstance();

        if (!ds.isAutonomous()) {
            Scheduler.getInstance().add(new TeleopPickupCommand());
            return true;
        }

        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}