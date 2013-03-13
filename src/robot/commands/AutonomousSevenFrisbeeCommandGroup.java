package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.subsystems.ShooterSubsystem;

public class AutonomousSevenFrisbeeCommandGroup extends CommandGroup {

    public AutonomousSevenFrisbeeCommandGroup() {
        addParallel(new AutonomousPickupCommand(true)); //Lower the pickup
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_PYRAMID_SETPOINT.get())); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.LOAD));

        addSequential(new AutonomousDriveCommand(Constants.SEVEN_FRISBEE_DRIVE_FORWARD_INCHES.get()));
        addSequential(new AutonomousDriveCommand(-Constants.SEVEN_FRISBEE_DRIVE_FORWARD_INCHES.get()));
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_PYRAMID_SETPOINT.get())); //This will be for setting the setpoint after we've reached our destination
        addSequential(new AutonomousHopperCommand(true)); //Shoot all frisbees until the end of autonomous mode
    }
}
