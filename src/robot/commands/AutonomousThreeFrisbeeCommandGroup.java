package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.subsystems.ShooterSubsystem;

public class AutonomousThreeFrisbeeCommandGroup extends CommandGroup {

    public AutonomousThreeFrisbeeCommandGroup() {
        addParallel(new AutonomousPickupCommand(true)); //Lower the pickup
        addParallel(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.MEDIUM, 1.0)); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
    }
}
