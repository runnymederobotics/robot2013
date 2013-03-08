package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.parsable.ParsableDouble;
import robot.subsystems.ShooterSubsystem;

public class AutonomousFiveFrisbeeCommandGroup extends CommandGroup {

    public static ParsableDouble DRIVE_FORWARD_DISTANCE_INCHES = new ParsableDouble("five_frisbee_drive_forward_inches", 90);
    
    public AutonomousFiveFrisbeeCommandGroup() {
        addParallel(new AutonomousPickupCommand(true)); //Lower the pickup
        addParallel(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.MEDIUM, 1.0)); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
        
        addSequential(new AutonomousDriveCommand(DRIVE_FORWARD_DISTANCE_INCHES.get())); //Drive forward
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, 0.5)); //This will be for setting the setpoint after we've reached our destination
        addSequential(new AutonomousHopperCommand(true)); //Shoot all frisbees until the end of autonomous mode
    }
}
