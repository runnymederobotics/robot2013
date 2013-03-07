package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.subsystems.ShooterSubsystem;

public class AutonomousFiveFrisbeeCommandGroup extends CommandGroup {

    public AutonomousFiveFrisbeeCommandGroup() {
        addParallel(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.MEDIUM, 1.0)); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
        addParallel(new AutonomousPickupCommand(true)); //Lower the pickup
        
        addSequential(new AutonomousDriveCommand(90)); //Drive forward
    }
}
