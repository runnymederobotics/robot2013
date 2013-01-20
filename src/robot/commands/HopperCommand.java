package robot.commands;

import edu.wpi.first.wpilibj.DriverStation;

public class HopperCommand extends CommandBase {

    public HopperCommand() {
        requires(hopperSubsystem);
    }

    protected void initialize() {
        hopperSubsystem.reset();
    }

    protected void execute() {
        if (DriverStation.getInstance().isOperatorControl()) {
            hopperSubsystem.update(oi.getRequestShot());
        } else {
            hopperSubsystem.update(true);
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
