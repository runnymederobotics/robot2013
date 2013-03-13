package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.subsystems.ShooterSubsystem;

public class AutonomousFiveFrisbeeCommandGroup extends CommandGroup {

    public AutonomousFiveFrisbeeCommandGroup() {
        addParallel(new AutonomousPickupCommand(true)); //Lower the pickup
        addParallel(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_PYRAMID_SETPOINT.get())); //Raise shooter
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
        addParallel(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.LOAD)); //Lower shooter

        addSequential(new AutonomousDriveCommand(Constants.FIVE_FRISBEE_DRIVE_FORWARD_INCHES.get())); //Drive forward
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_PYRAMID_SETPOINT.get())); //This will be for setting the setpoint after we've reached our destination
        addSequential(new AutonomousHopperCommand(true)); //Shoot all frisbees until the end of autonomous mode
    }
}
