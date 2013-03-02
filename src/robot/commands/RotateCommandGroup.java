package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RotateCommandGroup extends CommandGroup {

    public RotateCommandGroup() {
        addSequential(new AutonomousRotateCommand(-90));
    }
}
