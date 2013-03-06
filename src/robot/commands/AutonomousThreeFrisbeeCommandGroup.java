package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.subsystems.ShooterSubsystem;

public class AutonomousThreeFrisbeeCommandGroup extends CommandGroup {

    public AutonomousThreeFrisbeeCommandGroup() {
        addParallel(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.MEDIUM)); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand()); //Shoot all frisbees
        addParallel(new AutonomousPickupCommand(true)); //Lower the pickup
        
        addSequential(new AutonomousDriveCommand(90)); //Drive forward
    }
}
