package robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import robot.Constants;
import robot.subsystems.ShooterSubsystem;

public class AutonomousLeftPyramidCommandGroup extends CommandGroup {

    public AutonomousLeftPyramidCommandGroup() {
        addParallel(new AutonomousPickupCommand(true, false)); //Lower the pickup but don't run the roller
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_AUTONOMOUS_START_SETPOINT.get())); //Raise shooter to medium level
        addSequential(new AutonomousHopperCommand(false)); //Shoot all frisbees
        addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.LOAD, 0));
        
        addSequential(new AutonomousDriveCommand(-Constants.LEFT_PYRAMID_INITIAL_REVERSE_INCHES.get()));
        addSequential(new AutonomousCurveDriveCommand(-Constants.LEFT_PYRAMID_CURVE_INCHES.get(), Constants.LEFT_PYRAMID_CURVE_CONSTANT.get()));
        addParallel(new AutonomousPickupCommand(true, true)); //Start running pickup
        addSequential(new AutonomousDriveCommand(Constants.LEFT_PYRAMID_DRIVE_FORWARD_AFTER_CURVE_INCHES.get()));
        addSequential(new AutonomousDriveCommand(-Constants.LEFT_PYRAMID_REVERSE_AFTER_PICKUP_INCHES.get()));
        //addSequential(new AutonomousShooterCommand(ShooterSubsystem.ShooterState.HIGH, Constants.SHOOTER_PYRAMID_SETPOINT.get())); //This will be for setting the setpoint after we've reached our destination
        //addSequential(new AutonomousHopperCommand(true)); //Shoot all frisbees until the end of autonomous mode
    }
}