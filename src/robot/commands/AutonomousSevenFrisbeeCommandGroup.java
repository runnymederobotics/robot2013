package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.parsable.ParsableDouble;
import robot.subsystems.ChassisSubsystem;
import robot.subsystems.ShooterSubsystem;

public class AutonomousSevenFrisbeeCommandGroup extends CommandGroup {

    ParsableDouble DRIVE_FORWARD_DISTANCE_INCHES = new ParsableDouble("seven_frisbee_drive_forward_inches", 90);
    
    public AutonomousSevenFrisbeeCommandGroup() {
        addParallel(new AutonomousPickupCommand(true)); //Lower the pickup
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.MEDIUM, 0.5)); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, 0.5));
        
        //Drive forward the correct number of counts
        addSequential(new AutonomousDriveCommand((int) (DRIVE_FORWARD_DISTANCE_INCHES.get() / ChassisSubsystem.INCHES_PER_ENCODER_COUNT)));
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, 0.5)); //This will be for setting the setpoint after we've reached our destination
        addSequential(new AutonomousHopperCommand(true));
    }
}
